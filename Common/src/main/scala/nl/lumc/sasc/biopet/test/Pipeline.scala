package nl.lumc.sasc.biopet.test

import java.io.{ File, PrintWriter }

import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.SkipException
import org.testng.annotations.{ DataProvider, Test, BeforeClass }

import scala.io.Source
import scala.sys.process._
import scala.util.matching.Regex

/**
 * Created by pjvan_thof on 6/30/15.
 */

trait Pipeline extends TestNGSuite with Matchers {

  /** Output dir of pipeline */
  def outputDir = new File(Biopet.getOutputDir, this.getClass.getName.stripPrefix("nl.lumc.sasc.biopet.test."))

  /** The argument to queue for outputDir, default uses `outputDir` */
  def outputDirArg = Option(outputDir)

  /** Log file of pipeline */
  def logFile = new File(outputDir, "run.log")

  /** Name of pipeline, this is used for the commandline and for the summary */
  def pipelineName: String

  /** Args given on the commandline */
  def args: Seq[String]

  private var _exitValue = -1
  /** exitvalue of the pipeline, if this is -1 the pipeline is not executed yet */
  def exitValue = _exitValue

  /** Memory for biopet process */
  def memoryArg = "-Xmx120m"

  /** This is the retry option for the pipeline, default is 5 */
  def retries = Option(6)

  /**
   * Allowed retries, test will be generated from `allowRetries` until `retries`
   * Default is 0 so no retries are allowed
   */
  def allowRetries = 1

  /** This enabled the "--disablescatter" option on the commandline, default enabled */
  def disablescatter = true

  /** This are the config files given to the pipeline with the "-config" option */
  def configs: List[File] = Nil

  /**
   * When override this to true the pipeline is marked as functional.
   * This is only executed when property "biopet.functionalTests" is set true
   */
  def functionalTest = false

  /** This is default true, when override to false the pipeline will only execute a dry run */
  def run = true

  @BeforeClass
  def runPipeline(): Unit = {
    if (functionalTest && !Biopet.functionalTests) throw new SkipException("Functional tests are disabled")
    // Running pipeline
    _exitValue = Pipeline.runPipeline(this)
  }

  @DataProvider(name = "not_allowed_reties")
  def notAllowedRetries = {
    (for (i <- (allowRetries + 1) to retries.getOrElse(1)) yield {
      Array("d", i)
    }).toArray
  }

  @Test(dataProvider = "not_allowed_reties", dependsOnGroups = Array("parseLog"))
  def testRetry(dummy: String, retry: Int): Unit = {
    val s = s"Reset for retry attempt $retry of ${retries.getOrElse(0)}"
    require(!logLines.exists(_.contains(s)), s"${retry}e retry found but not allowed")
  }

  @Test(priority = -1) def exitcode() = exitValue shouldBe 0
  @Test def outputDirExist() = assert(outputDir.exists())

  private var _logLines: List[String] = _
  def logLines = _logLines

  @Test(groups = Array("parseLog"))
  def logFileExist() = {
    assert(logFile.exists())
    _logLines = Source.fromFile(logFile).getLines().toList
  }

  private var logMustHave: List[Regex] = Nil
  def logMustHave(r: Regex): Unit = logMustHave :+= r

  @DataProvider(name = "log_must_have")
  def logMustHaveProvider = logMustHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_have", dependsOnGroups = Array("parseLog"))
  def testLogMustHave(r: Regex): Unit = withClue(s"regex: $r") {
    assert(logLines.exists(r.findFirstMatchIn(_).isDefined), s"Logfile does not contains: $r")
  }

  private var logMustNotHave: List[Regex] = Nil
  def logMustNotHave(r: Regex): Unit = logMustNotHave :+= r

  @DataProvider(name = "log_must_not_have")
  def logMustNotHaveProvider = logMustNotHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_not_have", dependsOnGroups = Array("parseLog"))
  def testLogMustNotHave(r: Regex): Unit = withClue(s"regex: $r") {
    val i = logLines.indexWhere(r.findFirstMatchIn(_).isDefined)
    assert(i == -1, s"at line number ${i + 1} in logfile does contains: $r")
  }
}

object Pipeline {
  def runPipeline(pipeline: Pipeline) = {
    val cmd = Seq("java", pipeline.memoryArg, "-jar", Biopet.getBiopetJar.toString, "pipeline", pipeline.pipelineName) ++
      Biopet.queueArgs ++
      (if (pipeline.disablescatter) Seq("--disablescatter") else Seq()) ++
      cmdArg("-retry", pipeline.retries) ++
      cmdCondition("-run", pipeline.run) ++
      cmdConfig("output_dir", pipeline.outputDirArg) ++
      pipeline.configs.flatMap(x => Seq("-config", x.getAbsolutePath)) ++
      pipeline.args
    if (!pipeline.outputDir.exists()) pipeline.outputDir.mkdirs()

    if (pipeline.logFile.exists()) pipeline.logFile.delete()
    val writer = new PrintWriter(pipeline.logFile)
    def writeLine(line: String): Unit = {
      writer.println(line)
      writer.flush()
    }
    val process = Process(cmd, pipeline.outputDir).run(ProcessLogger(writeLine(_)))
    val exitValue = process.exitValue()
    writer.close()

    exitValue
  }

  def cmdArg(argName: String, value: Any): Seq[String] = {
    value match {
      case Some(v) => Seq(argName, v.toString)
      case None    => Seq()
      case _       => Seq(argName, value.toString)
    }
  }

  def cmdCondition(argName: String, condition: Boolean): Seq[String] = {
    if (condition) Seq(argName) else Seq()
  }

  def cmdConfig(configKey: String, value: Any): Seq[String] = {
    value match {
      case Some(v) => Seq("-cv", s"$configKey=$v")
      case None    => Seq()
      case _       => Seq("-cv", s"$configKey=$value")
    }
  }

}
