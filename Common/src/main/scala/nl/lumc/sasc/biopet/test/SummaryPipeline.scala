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
}
