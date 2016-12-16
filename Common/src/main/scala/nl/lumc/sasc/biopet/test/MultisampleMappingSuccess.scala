package nl.lumc.sasc.biopet.test

import java.io.File

import org.json4s._
import org.testng.annotations.Test

/**
 * Created by pjvanthof on 25/01/16.
 */
trait MultisampleMappingSuccess extends MultisampleMapping with MultisampleSuccess {
  def libraryBam(sampleId: String, libId: String) = new File(libraryDir(sampleId, libId), s"$sampleId-$libId.dedup.bam")

  def libraryPreprecoessBam(sampleId: String, libId: String) = libraryBam(sampleId, libId)

  def sampleBam(sampleId: String) = new File(sampleDir(sampleId), s"$sampleId.bam")

  def samplePreprocessBam(sampleId: String) = sampleBam(sampleId)

  def paired: Boolean

  def shouldHaveKmerContent: Option[Boolean]

  override def summarySchemaUrls = super.summarySchemaUrls ++ Seq(
    "/schemas/flexiprep.json",
    "/schemas/bammetrics.json")

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibraryBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ pipelineName \ "files" \ "pipeline" \ "output_bam" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe libraryBam(sample, lib)
    val replacejob = new File(libraryDir(sample, lib), s".$sample-$lib.final.bam.addorreplacereadgroups.out")
    file should not be exist
  }

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibraryPreprocessBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ pipelineName \ "files" \ "pipeline" \ "output_bam_preprocess" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe libraryPreprecoessBam(sample, lib)
    if (samples(sample).size == 1) {
      assert(file.exists())
    } else {
      val mappingBam = summary \ "samples" \ sample \ "libraries" \ lib \ pipelineName \ "files" \ "pipeline" \ "output_bam" \ "path"
      if (mappingBam != summaryPath) assert(!file.exists())
    }
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSampleBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val summaryPath = summary \ "samples" \ sample \ pipelineName \ "files" \ "pipeline" \ "output_bam" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe sampleBam(sample)
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSamplePrepreocessBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val summaryPath = summary \ "samples" \ sample \ pipelineName \ "files" \ "pipeline" \ "output_bam_preprocess" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe samplePreprocessBam(sample)

    if (samples(sample).size == 1 && sampleBam(sample) == file) assert(java.nio.file.Files.isSymbolicLink(file.toPath))

    assert(file.getName.startsWith(s"$sample."))
    assert(file.exists())
  }

  addMustHaveFile("report", "Files")
  addMustHaveFile("report", "Files", "index.html")

  addMustHaveFile("report", "Reference")
  addMustHaveFile("report", "Reference", "index.html")

  addMustHaveFile("report", "alignmentSummary.tsv")
  addMustHaveFile("report", "alignmentSummary.png")

  addConditionalFile(flexiprepShouldRun, "report", "QC_Bases_R1.png")
  addConditionalFile(flexiprepShouldRun, "report", "QC_Reads_R1.png")
  addConditionalFile(flexiprepShouldRun, "report", "QC_Bases_R1.tsv")
  addConditionalFile(flexiprepShouldRun, "report", "QC_Reads_R1.tsv")

  addConditionalFile(flexiprepShouldRun && paired, "report", "QC_Bases_R2.png")
  addConditionalFile(flexiprepShouldRun && paired, "report", "QC_Reads_R2.png")
  addConditionalFile(flexiprepShouldRun && paired, "report", "QC_Bases_R2.tsv")
  addConditionalFile(flexiprepShouldRun && paired, "report", "QC_Reads_R2.tsv")

  addConditionalFile(paired, "report", "insertsize.png")
  addConditionalFile(paired, "report", "insertsize.tsv")

  addConditionalFile(rnaMetricsShouldRun, "report", "rna.png")
  addConditionalFile(rnaMetricsShouldRun, "report", "rna.tsv")

  addConditionalFile(wgsMetricsShouldRun, "report", "wgs.png")
  addConditionalFile(wgsMetricsShouldRun, "report", "wgs.tsv")

  samples.foreach {
    case (sample, libraries) =>
      addMustHaveFile("report", "Samples", sample, "alignmentSummary.tsv")
      addMustHaveFile("report", "Samples", sample, "alignmentSummary.png")
      addMustHaveFile("report", "Samples", sample, "Alignment", "index.html")

      addConditionalFile(paired, "report", "Samples", sample, "Alignment", "insertsize.png")
      addConditionalFile(paired, "report", "Samples", sample, "Alignment", "insertsize.tsv")

      addConditionalFile(rnaMetricsShouldRun, "report", "Samples", sample, "Alignment", "rna.png")
      addConditionalFile(rnaMetricsShouldRun, "report", "Samples", sample, "Alignment", "rna.tsv")

      addConditionalFile(wgsMetricsShouldRun, "report", "Samples", sample, "Alignment", "wgs.png")
      addConditionalFile(wgsMetricsShouldRun, "report", "Samples", sample, "Alignment", "wgs.tsv")

      libraries.foreach { library =>
        addConditionalFile(paired, "report", "Samples", sample, "Libraries", library, "Alignment", "insertsize.png")
        addConditionalFile(paired, "report", "Samples", sample, "Libraries", library, "Alignment", "insertsize.tsv")

        addMustHaveFile("report", "Samples", sample, "Libraries", library, "Alignment", "index.html")

        addConditionalFile(rnaMetricsShouldRun, "report", "Samples", sample, "Libraries", library, "Alignment", "rna.png")
        addConditionalFile(rnaMetricsShouldRun, "report", "Samples", sample, "Libraries", library, "Alignment", "rna.tsv")

        addConditionalFile(wgsMetricsShouldRun, "report", "Samples", sample, "Libraries", library, "Alignment", "wgs.png")
        addConditionalFile(wgsMetricsShouldRun, "Samples", sample, "Libraries", library, "Alignment", "wgs.tsv")

        shouldHaveKmerContent match {
          case Some(shouldHaveKmerContent) =>
            addConditionalFile(flexiprepShouldRun && shouldHaveKmerContent, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_kmer_profiles.png")
            addConditionalFile(flexiprepShouldRun && shouldHaveKmerContent, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_kmer_profiles.png")
            addConditionalFile(flexiprepShouldRun && paired && shouldHaveKmerContent, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_kmer_profiles.png")
            addConditionalFile(flexiprepShouldRun && paired && shouldHaveKmerContent, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_kmer_profiles.png")
          case _ =>
        }
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "index.html")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_duplication_levels.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_base_quality.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_base_sequence_content.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_sequence_gc_content.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_sequence_quality.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_duplication_levels.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_base_quality.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_base_sequence_content.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_sequence_gc_content.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_sequence_quality.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_sequence_length_distribution.png")
        addConditionalFile(flexiprepShouldRun, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R1_sequence_length_distribution.png")

        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_duplication_levels.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_base_quality.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_base_sequence_content.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_sequence_gc_content.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_sequence_quality.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_duplication_levels.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_base_quality.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_base_sequence_content.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_sequence_gc_content.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_sequence_quality.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_sequence_length_distribution.png")
        addConditionalFile(flexiprepShouldRun && paired, "report", "Samples", sample, "Libraries", library, "QC", "fastqc_R2_sequence_length_distribution.png")
      }
  }
}
