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
  def outputDir = new File(Biopet.getOutputDir, this.getClass.getName.stripPrefix("nl.lumc.sasc.biopet.test."))

  /** Given token(s) of a filesystem path that points to an output file, return its file object representation. */
  def getOutputFile(pathTokens: String*): File =
    new File(outputDir, pathTokens.mkString(File.separator))

  def logFile = new File(outputDir, "run.log")

  def pipelineName: String

  def args: Seq[String]

  private var _exitValue = -1
  def exitValue = _exitValue

  def memoryArg = "-Xmx150m"

  def retries = Option(5)
  def allowRetries = 0

  /**
   * When override this to true the pipeline is marked as functional.
   * This is only executed when property "biopet.functionalTests" is set true
   */
  def functionalTest = false

  /** This is default true, when override to false the pipeline will only execute a dry run */
  def run = true

  @BeforeClass
  def runPipeline: Unit = {
    if (functionalTest && !Biopet.functionalTests) throw new SkipException("Functional tests are disabled")
    // Running pipeline
    _exitValue = Pipeline.runPipeline(pipelineName, outputDir, args, logFile, memoryArg, retries, run)
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

  @Test(priority = -1) def exitcode = exitValue shouldBe 0
  @Test def outputDirExist = assert(outputDir.exists())

  private var _logLines: List[String] = _
  def logLines = _logLines

  @Test(groups = Array("parseLog"))
  def logFileExist = {
    assert(logFile.exists())
    _logLines = Source.fromFile(logFile).getLines().toList
  }

  private var logMustHave: List[Regex] = Nil
  def logMustHave(r: Regex): Unit = logMustHave :+= r

  @DataProvider(name = "log_must_have")
  private def logMustHaveProvider = logMustHave.toArray

  @Test(dataProvider = "log_must_have", dependsOnGroups = Array("parseLog"))
  def testLogMustHave(r: Regex): Unit = {
    assert(logLines.exists(r.findFirstMatchIn(_).isDefined), s"Logfile does not contains: $r")
  }

  private var logMustNotHave: List[Regex] = Nil
  def logMustNotHave(r: Regex): Unit = logMustNotHave :+= r

  @DataProvider(name = "log_must_not_have")
  private def logMustNotHaveProvider = logMustNotHave.toArray

  @Test(dataProvider = "log_must_not_have", dependsOnGroups = Array("parseLog"))
  def testLogMustNotHave(r: Regex): Unit = {
    val i = logLines.indexWhere(r.findFirstMatchIn(_).isDefined)
    assert(i == -1, s"at line number ${i + 1} in logfile does contains: $r")
  }

}

object Pipeline {
  def runPipeline(pipelineName: String,
                  outputDir: File, args: Seq[String],
                  logFile: File,
                  memoryArg: String,
                  retries: Option[Int],
                  run: Boolean) = {
    val cmd = Seq("java", memoryArg, "-jar", Biopet.getBiopetJar.toString, "pipeline", pipelineName) ++
      args ++ Biopet.queueArgs ++ retries.map(r => Seq("-retry", r.toString)).getOrElse(Seq()) ++
      (if (run) Seq("-run") else Seq())
    if (!outputDir.exists()) outputDir.mkdirs()

    if (logFile.exists()) logFile.delete()
    val writer = new PrintWriter(logFile)
    def writeLine(line: String): Unit = {
      writer.println(line)
      writer.flush()
    }
    val process = Process(cmd, outputDir).run(ProcessLogger(writeLine(_)))
    val exitValue = process.exitValue()
    writer.close()

    exitValue
  }
}