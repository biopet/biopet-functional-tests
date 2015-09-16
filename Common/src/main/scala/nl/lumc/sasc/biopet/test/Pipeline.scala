package nl.lumc.sasc.biopet.test

import java.io.{ PrintWriter, File }

import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.SkipException
import org.testng.annotations.{ DataProvider, Test, BeforeClass }

import scala.io.Source
import scala.sys.process._

/**
 * Created by pjvan_thof on 6/30/15.
 */

trait Pipeline extends TestNGSuite with Matchers {
  def outputDir = new File(Biopet.getOutputDir, this.getClass.getName.stripPrefix("nl.lumc.sasc.biopet.test."))

  def logFile = new File(outputDir, "run.log")

  def pipelineName: String

  def args: Seq[String]

  private var _exitValue = -1
  def exitValue = _exitValue

  def memoryArg = "-Xmx150m"

  def retries = Option(5)
  def allowRetries = 0

  val functionalTest = true

  @BeforeClass
  def beforeTest: Unit = {
    if (functionalTest && !Biopet.functionalTests) throw new SkipException("Functional tests are disabled")
    // Running pipeline
    _exitValue = Pipeline.runPipeline(pipelineName, outputDir, args, logFile, memoryArg, retries)
  }

  @DataProvider(name = "not_allowed_reties")
  def notAllowedRetries = {
    (for (i <- (allowRetries + 1) to retries.getOrElse(1)) yield {
      Array("d", i)
    }).toArray
  }

  @Test(dataProvider = "not_allowed_reties")
  def testRetry(dummy: String, retry: Int): Unit = {
    val s = s"Reset for retry attempt $retry of ${retries.getOrElse(0)}"
    require(!Source.fromFile(logFile).getLines().exists(_.contains(s)), s"${retry}e retry found but not allowed")
  }

  @Test(priority = -1) def exitcode = exitValue shouldBe 0
  @Test def outputDirExist = assert(outputDir.exists())
  @Test def logFileExist = assert(logFile.exists())
}

object Pipeline {
  def runPipeline(pipelineName: String,
                  outputDir: File, args: Seq[String],
                  logFile: File,
                  memoryArg: String,
                  retries: Option[Int]) = {
    val cmd = Seq("java", memoryArg, "-jar", Biopet.getBiopetJar.toString, "pipeline", pipelineName) ++
      args ++ Biopet.queueArgs ++ retries.map(r => Seq("-retry", r.toString)).getOrElse(Seq())
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