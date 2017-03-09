package nl.lumc.sasc.biopet.test

import java.io.File

import nl.lumc.sasc.biopet.test.samples.Samples
import org.testng.annotations.{ DataProvider, Test }

import scala.concurrent.Await
import scala.concurrent.duration.Duration

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

  @Test(dataProvider = "samples", dependsOnGroups = Array("summary"))
  def testSampleSummary(sampleId: String): Unit = withClue(s"sample = $sampleId") {
    val sample = Await.result(summaryDb.getSamples(name = sampleId), Duration.Inf).headOption
    assert(sample.isDefined, s"Sample $sampleId does not exist in summary")
  }

  @Test(dataProvider = "libraries", dependsOnGroups = Array("summary"))
  def testLibrarySummary(sampleId: String, libId: String): Unit = withClue(s"sample = $sampleId; library = $libId") {
    val lib = Await.result(summaryDb.getLibraries(name = libId), Duration.Inf).headOption
    assert(lib.isDefined, s"Library $sampleId -> $libId does not exist in summary")
  }

  samples.foreach {
    case (sample, libraries) =>
      addMustHaveFile("report", "Samples", sample)
      addMustHaveFile("report", "Samples", sample, "index.html")
      addMustHaveFile("samples", sample)

      libraries.foreach { library =>
        addMustHaveFile("report", "Samples", sample, "Libraries", library)
        addMustHaveFile("report", "Samples", sample, "Libraries", library, "index.html")
        addMustHaveFile("samples", sample, s"lib_$library")
      }
  }

}
