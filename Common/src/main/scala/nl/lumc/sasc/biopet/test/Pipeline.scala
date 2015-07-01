package nl.lumc.sasc.biopet.test

import java.io.File

import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.BeforeClass

import scala.sys.process._

/**
 * Created by pjvan_thof on 6/30/15.
 */
trait Pipeline extends TestNGSuite with Matchers {
  val outputDir = new File(Biopet.getOutputDir, getClass.getName)

  def pipelineName: String

  def args: Seq[String]

  def pipelineCmd = Seq("java", "-jar", Biopet.getBiopetJar.toString, "pipeline", pipelineName) ++ args ++ Biopet.queueArgs
  private var _exitValue = -1
  def exitValue = _exitValue

  @BeforeClass
  def beforeTest: Unit = {
    // Running pipeline
    outputDir.mkdir()
    val process = Process(pipelineCmd, outputDir).run()
    _exitValue = process.exitValue()
  }
}
