package nl.lumc.sasc.biopet.test

import java.io.{ PrintWriter, File }

import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{ Test, BeforeClass }

import scala.sys.process._

/**
 * Created by pjvan_thof on 6/30/15.
 */
trait Pipeline extends TestNGSuite with Matchers {
  val outputDir = new File(Biopet.getOutputDir, this.getClass.getName)

  def pipelineName: String

  def args: Seq[String]

  private var _exitValue = -1
  def exitValue = _exitValue

  @BeforeClass
  def beforeTest: Unit = {
    // Running pipeline
    _exitValue = Pipeline.runPipeline(pipelineName, outputDir, args)
  }

  @Test(priority = -1) def exitcode = exitValue shouldBe 0
  @Test def outputDirExist = assert(outputDir.exists())
  @Test def logFileExist = assert(new File(outputDir, "run.log").exists())

}

object Pipeline {
  def runPipeline(pipelineName: String, outputDir: File, args: Seq[String]) = {
    val cmd = Seq("java", "-Xmx512m", "-jar", Biopet.getBiopetJar.toString, "pipeline", pipelineName) ++ args ++ Biopet.queueArgs
    if (!outputDir.exists()) outputDir.mkdir()

    val logFile = new File(outputDir, "run.log")
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