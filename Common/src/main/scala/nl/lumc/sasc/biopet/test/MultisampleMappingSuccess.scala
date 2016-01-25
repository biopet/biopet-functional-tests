package nl.lumc.sasc.biopet.test

import java.io.File

import htsjdk.samtools.SamReaderFactory
import org.json4s._
import org.testng.annotations.Test

/**
  * Created by pjvanthof on 25/01/16.
  */
trait MultisampleMappingSuccess extends MultisampleSuccess {
  def libraryBam(sampleId: String, libId: String) = new File(libraryDir(sampleId, libId), s"$sampleId-$libId.final.bam")

  def libraryPreprecoessBam(sampleId: String, libId: String) = libraryBam(sampleId, libId)

  def sampleBam(sampleId: String) = new File(sampleDir(sampleId), s"$sampleId.bam")

  def samplePreprocessBam(sampleId: String) = sampleBam(sampleId)

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibraryBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ "shiva" \ "files" \ "pipeline" \ "output_bam" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe libraryBam(sample, lib)
    val replacejob = new File(libraryDir(sample, lib), s".$sample-$lib.final.bam.addorreplacereadgroups.out")
    if (replacejob.exists()) assert(!file.exists())
    else assert(file.exists())
  }

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibraryPreprocessBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ "shiva" \ "files" \ "pipeline" \ "output_bam_preprocess" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe libraryPreprecoessBam(sample, lib)
    if (samples(sample).size == 1) {
      assert(file.exists())
    } else {
      val mappingBam = summary \ "samples" \ sample \ "libraries" \ lib \ "shiva" \ "files" \ "pipeline" \ "output_bam" \ "path"
      if (mappingBam != summaryPath) assert(!file.exists())
    }
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSampleBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val summaryPath = summary \ "samples" \ sample \ "shiva" \ "files" \ "pipeline" \ "output_bam_preprocess" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe sampleBam(sample)
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSamplePrepreocessBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val summaryPath = summary \ "samples" \ sample \ "shiva" \ "files" \ "pipeline" \ "output_bam_preprocess" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe samplePreprocessBam(sample)
    assert(file.getName.startsWith(s"$sample."))
    assert(file.exists())
  }

}
