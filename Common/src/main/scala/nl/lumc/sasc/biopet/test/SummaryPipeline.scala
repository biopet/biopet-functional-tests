package nl.lumc.sasc.biopet.test

import java.io.File

import org.testng.annotations.Test
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
 * Created by pjvanthof on 19/09/15.
 */
trait SummaryPipeline extends Pipeline {

  implicit val formats = DefaultFormats

  def summaryFile: File

  private var _summary: JValue = _
  def summary = _summary

  @Test(groups = Array("parseSummary"))
  def parseSummary(): Unit = _summary = parse(summaryFile)

  @Test(groups = Array("parseSummary"))
  def testSummaryFileExist(): Unit = assert(summaryFile.exists, "Summary file does not exist")

  // Testing meta field of summary

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryPipelineName(): Unit =
    (summary \ "meta" \ "pipeline_name").extract[String] shouldBe pipelineName.toLowerCase

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryPipelineVersion(): Unit =
    (summary \ "meta" \ "pipeline_version").extractOpt[String] shouldBe defined

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCommitHash(): Unit =
    (summary \ "meta" \ "last_commit_hash").extractOpt[String] shouldBe defined

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryOutputDir(): Unit =
    (summary \ "meta" \ "output_dir").extract[String] shouldBe outputDir.getAbsolutePath

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCreation(): Unit =
    (summary \ "meta" \ "summary_creation").extractOpt[String] shouldBe defined

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryRunName(): Unit =
    (summary \ "meta" \ "run_name").extractOpt[String] shouldBe defined
}