package nl.lumc.sasc.biopet.test

import java.io.File

import nl.lumc.sasc.biopet.utils.summary.Summary
import org.testng.annotations.{ Test }

/**
 * Created by pjvanthof on 19/09/15.
 */
trait SummaryPipeline extends Pipeline {
  def summaryFile: File

  private var _summary: Summary = _
  def summary = _summary

  @Test(groups = Array("parseSummary"))
  def parseSummary: Unit = {
    _summary = new Summary(summaryFile)
  }

  @Test(groups = Array("parseSummary"))
  def testSummaryFileExist: Unit = {
    assert(summaryFile.exists(), "Summary file does not exist")
  }

  // Testing meta field of summary

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryPipelineName: Unit = {
    summary.getValue("meta", "pipeline_name") shouldBe Some(pipelineName.toLowerCase)
  }

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryPipelineVersion: Unit = {
    summary.getValue("meta", "pipeline_version") should not be None
  }

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCommitHash: Unit = {
    summary.getValue("meta", "last_commit_hash") should not be None
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryOutputDir: Unit = {
    summary.getValue("meta", "output_dir") shouldBe Some(outputDir.getAbsolutePath)
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCreation: Unit = {
    summary.getValue("meta", "summary_creation") should not be None
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryRunName: Unit = {
    summary.getValue("meta", "run_name") should not be None
  }
}