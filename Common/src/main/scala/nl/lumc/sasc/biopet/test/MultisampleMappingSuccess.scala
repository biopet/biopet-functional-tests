package nl.lumc.sasc.biopet.test

import java.io.File

import org.json4s._
import org.testng.annotations.Test
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb.Implicts._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

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

  def rootPipelineGroup(sample: Option[String] = None, library: Option[String] = None) =
    SummaryGroup(pipelineName, sample = sample, library = library)

  @Test(dataProvider = "libraries", dependsOnGroups = Array("summary"))
  def testLibraryBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val dbFile = Await.result(summaryDb.getFile(runId, pipelineName, sample = sample, library = lib, key = "output_bam"), Duration.Inf).headOption
    assert(dbFile.isDefined, s"output_bam for $sample -> $lib should be in the summary")
    val file = new File(dbFile.get.path)
    file shouldBe libraryBam(sample, lib)
    val replacejob = new File(libraryDir(sample, lib), s".$sample-$lib.final.bam.addorreplacereadgroups.out")
    if (samples(sample).size > 1 || libraryBam(sample, lib) != libraryPreprecoessBam(sample, lib))
      file should not be exist
    else file should exist
  }

  @Test(dataProvider = "libraries", dependsOnGroups = Array("summary"))
  def testLibraryPreprocessBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val dbFile = Await.result(summaryDb.getFile(runId, pipelineName, sample = sample, library = lib, key = "output_bam_preprocess"), Duration.Inf).headOption
    assert(dbFile.isDefined, s"output_bam for $sample -> $lib should be in the summary")
    val file = new File(dbFile.get.path)
    file shouldBe libraryPreprecoessBam(sample, lib)
    if (samples(sample).size == 1) {
      assert(file.exists())
    }
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("summary"))
  def testSampleBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val dbFile = Await.result(summaryDb.getFile(runId, pipelineName, sample = sample, key = "output_bam"), Duration.Inf).headOption
    assert(dbFile.isDefined, s"output_bam for $sample should be in the summary")
    val file = new File(dbFile.get.path)
    file shouldBe sampleBam(sample)
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSamplePrepreocessBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val dbFile = Await.result(summaryDb.getFile(runId, pipelineName, sample = sample, key = "output_bam_preprocess"), Duration.Inf).headOption
    assert(dbFile.isDefined, s"output_bam for $sample should be in the summary")
    val file = new File(dbFile.get.path)
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
        addConditionalFile(wgsMetricsShouldRun, "report", "Samples", sample, "Libraries", library, "Alignment", "wgs.tsv")

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
