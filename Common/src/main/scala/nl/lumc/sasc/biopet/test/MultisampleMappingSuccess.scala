package nl.lumc.sasc.biopet.test

import java.io.File

import org.json4s._
import org.testng.annotations.Test

/**
 * Created by pjvanthof on 25/01/16.
 */
trait MultisampleMappingSuccess extends MultisampleMapping with MultisampleSuccess {
  def libraryBam(sampleId: String, libId: String) = new File(libraryDir(sampleId, libId), s"$sampleId-$libId.final.bam")

  def libraryPreprecoessBam(sampleId: String, libId: String) = libraryBam(sampleId, libId)

  def sampleBam(sampleId: String) = new File(sampleDir(sampleId), s"$sampleId.bam")

  def samplePreprocessBam(sampleId: String) = sampleBam(sampleId)

  def paired: Boolean

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibraryBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ pipelineName \ "files" \ "pipeline" \ "output_bam" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file shouldBe libraryBam(sample, lib)
    val replacejob = new File(libraryDir(sample, lib), s".$sample-$lib.final.bam.addorreplacereadgroups.out")
    if (replacejob.exists()) assert(!file.exists())
    else assert(file.exists())
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

  addMustHaveReportFile("Files")
  addMustHaveReportFile("Files", "index.html")

  addMustHaveReportFile("Reference")
  addMustHaveReportFile("Reference", "index.html")

  addMustHaveReportFile("alignmentSummary.tsv")
  addMustHaveReportFile("alignmentSummary.png")

  addConditionalReportFile(flexiprepShouldRun, "QC_Bases_R1.png")
  addConditionalReportFile(flexiprepShouldRun, "QC_Reads_R1.png")
  addConditionalReportFile(flexiprepShouldRun, "QC_Bases_R1.tsv")
  addConditionalReportFile(flexiprepShouldRun, "QC_Reads_R1.tsv")

  addConditionalReportFile(flexiprepShouldRun && paired, "QC_Bases_R2.png")
  addConditionalReportFile(flexiprepShouldRun && paired, "QC_Reads_R2.png")
  addConditionalReportFile(flexiprepShouldRun && paired, "QC_Bases_R2.tsv")
  addConditionalReportFile(flexiprepShouldRun && paired, "QC_Reads_R2.tsv")

  addConditionalReportFile(paired, "insertsize.png")
  addConditionalReportFile(paired, "insertsize.tsv")

  addConditionalReportFile(rnaMetricsShouldRun, "rna.png")
  addConditionalReportFile(rnaMetricsShouldRun, "rna.tsv")

  addConditionalReportFile(wgsMetricsShouldRun, "wgs.png")
  addConditionalReportFile(wgsMetricsShouldRun, "wgs.tsv")

  samples.foreach {
    case (sample, libraries) =>
      addMustHaveReportFile("Samples", sample, "alignmentSummary.tsv")
      addMustHaveReportFile("Samples", sample, "alignmentSummary.png")
      addMustHaveReportFile("Samples", sample, "Alignment", "index.html")

      addConditionalReportFile(paired, "Samples", sample, "Alignment", "insertsize.png")
      addConditionalReportFile(paired, "Samples", sample, "Alignment", "insertsize.tsv")

      addConditionalReportFile(rnaMetricsShouldRun, "Samples", sample, "Alignment", "rna.png")
      addConditionalReportFile(rnaMetricsShouldRun, "Samples", sample, "Alignment", "rna.tsv")

      addConditionalReportFile(wgsMetricsShouldRun, "Samples", sample, "Alignment", "wgs.png")
      addConditionalReportFile(wgsMetricsShouldRun, "Samples", sample, "Alignment", "wgs.tsv")

      libraries.foreach { library =>
        addConditionalReportFile(paired, "Samples", sample, "Libraries", library, "Alignment", "insertsize.png")
        addConditionalReportFile(paired, "Samples", sample, "Libraries", library, "Alignment", "insertsize.tsv")

        addMustHaveReportFile("Samples", sample, "Libraries", library, "Alignment", "index.html")

        addConditionalReportFile(rnaMetricsShouldRun, "Samples", sample, "Libraries", library, "Alignment", "rna.png")
        addConditionalReportFile(rnaMetricsShouldRun, "Samples", sample, "Libraries", library, "Alignment", "rna.tsv")

        addConditionalReportFile(wgsMetricsShouldRun, "Samples", sample, "Libraries", library, "Alignment", "wgs.png")
        addConditionalReportFile(wgsMetricsShouldRun, "Samples", sample, "Libraries", library, "Alignment", "wgs.tsv")

        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "index.html")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_duplication_levels.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_kmer_profiles.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_base_quality.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_base_sequence_content.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_sequence_gc_content.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_per_sequence_quality.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_duplication_levels.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_kmer_profiles.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_base_quality.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_base_sequence_content.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_sequence_gc_content.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_per_sequence_quality.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_qc_sequence_length_distribution.png")
        addConditionalReportFile(flexiprepShouldRun, "Samples", sample, "Libraries", library, "QC", "fastqc_R1_sequence_length_distribution.png")

        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_duplication_levels.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_kmer_profiles.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_base_quality.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_base_sequence_content.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_sequence_gc_content.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_per_sequence_quality.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_duplication_levels.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_kmer_profiles.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_base_quality.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_base_sequence_content.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_sequence_gc_content.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_per_sequence_quality.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_qc_sequence_length_distribution.png")
        addConditionalReportFile(flexiprepShouldRun && paired, "Samples", sample, "Libraries", library, "QC", "fastqc_R2_sequence_length_distribution.png")
      }
  }
}
