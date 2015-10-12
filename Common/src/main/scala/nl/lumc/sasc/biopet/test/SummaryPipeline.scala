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
    (summary \ "meta" \ "pipeline_name") shouldBe JString(pipelineName.toLowerCase)

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryPipelineVersion(): Unit =
    (summary \ "meta" \ "pipeline_version") shouldBe a[JString]

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCommitHash(): Unit =
    (summary \ "meta" \ "last_commit_hash") shouldBe a[JString]

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryOutputDir(): Unit =
    (summary \ "meta" \ "output_dir").extract[String] shouldBe outputDir.getAbsolutePath

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCreation(): Unit =
    (summary \ "meta" \ "summary_creation") shouldBe a[JInt]

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryRunName(): Unit =
    (summary \ "meta" \ "run_name") shouldBe a[JString]

  def validateSummaryFile(summaryFile: JValue,
                          file: Option[File] = None,
                          md5: Option[String] = None): Unit = {
    summaryFile shouldBe a[JObject]
    (summaryFile \ "path") shouldBe a[JString]
    (summaryFile \ "md5") shouldBe a[JString]
    file.foreach(x => (summaryFile \ "path") shouldBe JString(x.getAbsolutePath))
    md5.foreach(x => (summaryFile \ "md5") shouldBe JString(x))
  }
}