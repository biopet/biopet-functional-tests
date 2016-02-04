package nl.lumc.sasc.biopet.test

import java.io.File

import nl.lumc.sasc.biopet.test.samples.Samples
import org.json4s._
import org.testng.annotations.{ DataProvider, Test }

/**
 * Created by pjvan_thof on 10/19/15.
 */
trait MultisampleSuccess extends SummaryPipeline with Samples with PipelineSuccess with Report {
  @DataProvider(name = "samples")
  def sampleNames = samples.keySet.map(Array(_)).toArray

  @DataProvider(name = "libraries")
  def libraryNames = samples.flatMap(sample => sample._2.map(Array(sample._1, _))).toArray

  def sampleDir(sampleId: String) = new File(outputDir, "samples" + File.separator + sampleId)

  def libraryDir(sampleId: String, libId: String) = new File(sampleDir(sampleId), s"lib_$libId")

  @Test(dataProvider = "samples")
  def testSampleDir(sampleId: String): Unit = withClue(s"sample = $sampleId") {
    assert(sampleDir(sampleId).exists())
  }

  @Test(dataProvider = "libraries")
  def testLibDir(sampleId: String, libId: String): Unit = withClue(s"sample = $sampleId; library = $libId") {
    assert(libraryDir(sampleId, libId).exists())
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSampleSummary(sampleId: String): Unit = withClue(s"sample = $sampleId") {
    summary \ "samples" \ sampleId shouldBe a[JObject]
  }

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibrarySummary(sampleId: String, libId: String): Unit = withClue(s"sample = $sampleId; library = $libId") {
    summary \ "samples" \ sampleId \ "libraries" \ libId shouldBe a[JObject]
  }

  samples.foreach { case (sample, libraries) =>
    addMustHaveReportFile("Samples", sample)
    addMustHaveReportFile("Samples", sample, "index.html")

    libraries.foreach { library =>
      addMustHaveReportFile("Samples", sample, "Libraries", library)
      addMustHaveReportFile("Samples", sample, "Libraries", library, "index.html")
    }
  }

}
