package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import org.testng.annotations.{ DataProvider, Test }

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.aligners.Gsnap
import nl.lumc.sasc.biopet.test.samples.{ Rna1, Rna2 }
import nl.lumc.sasc.biopet.test.references.HsapiensGRCh38
import nl.lumc.sasc.biopet.test.utils.pearsonScore

trait GentrapFunctional extends Gentrap with Rna1 with Rna2 {

  require(samples.keys.toList.sorted == List("rna1", "rna2"),
    "This trait can only be used together with sample rna1 and rna2")

  @Test
  def testFragmentsPerGene(): Unit = {
    if (this.isInstanceOf[FragmentsPerGene]) {
      val fixtureFile = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.fragments_per_gene")
      val pipelineFile = new File(outputDir, "expression_measures/fragmentspergene/fragmentspergene.fragments_per_gene.tsv")
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  @DataProvider(name = "basecounts")
  def baseCountsProvider = {
    Array("basecounts.exonAntiSenseCounts.tsv",
      "basecounts.exonCounts.tsv",
      "basecounts.exonSenseCounts.tsv",
      "basecounts.geneExonicAntiSenseCounts.tsv",
      "basecounts.geneExonicCounts.tsv",
      "basecounts.geneExonicSenseCounts.tsv",
      "basecounts.geneIntronicAntiSenseCounts.tsv",
      "basecounts.geneIntronicCounts.tsv",
      "basecounts.geneIntronicSenseCounts.tsv",
      "basecounts.geneTotalAntiSenseCounts.tsv",
      "basecounts.geneTotalCounts.tsv",
      "basecounts.geneTotalSenseCounts.tsv",
      "basecounts.intronAntiSenseCounts.tsv",
      "basecounts.intronCounts.tsv",
      "basecounts.intronSenseCounts.tsv",
      "basecounts.mergeExonAntiSenseCounts.tsv",
      "basecounts.mergeExonCounts.tsv",
      "basecounts.mergeExonSenseCounts.tsv",
      "basecounts.mergeIntronAntiSenseCounts.tsv",
      "basecounts.mergeIntronCounts.tsv",
      "basecounts.mergeIntronSenseCounts.tsv",
      "basecounts.nonStrandedMetaExonCounts.tsv",
      "basecounts.strandedAntiSenseMetaExonCounts.tsv",
      "basecounts.strandedMetaExonCounts.tsv",
      "basecounts.strandedSenseMetaExonCounts.tsv",
      "basecounts.transcriptExonicAntiSenseCounts.tsv",
      "basecounts.transcriptExonicCounts.tsv",
      "basecounts.transcriptExonicSenseCounts.tsv",
      "basecounts.transcriptIntronicAntiSenseCounts.tsv",
      "basecounts.transcriptIntronicCounts.tsv",
      "basecounts.transcriptIntronicSenseCounts.tsv",
      "basecounts.transcriptTotalAntiSenseCounts.tsv",
      "basecounts.transcriptTotalCounts.tsv",
      "basecounts.transcriptTotalSenseCounts.tsv"
    ).map(x => Array(Biopet.fixtureFile("gentrap", "count_table", "func1", "basecounts", x),
        new File(outputDir, "expression_measures/basecounts/" + x)))
  }

  @Test(dataProvider = "basecounts")
  def testBaseCounts(fixtureFile: File, pipelineFile: File): Unit = {
    if (this.isInstanceOf[BaseCounts]) {
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  @Test
  def testCufflinksStrictGenes(): Unit = {
    if (this.isInstanceOf[CufflinksStrict]) {
      val fixtureFile = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.genes_fpkm_cufflinks_strict")
      val pipelineFile = new File(outputDir, "expression_measures/cufflinksstrict/cufflinksstrict.genes.fpkm.tsv")
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  @Test
  def testCufflinksStrictIsoform(): Unit = {
    if (this.isInstanceOf[CufflinksStrict]) {
      val fixtureFile = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.isoforms_fpkm_cufflinks_strict")
      val pipelineFile = new File(outputDir, "expression_measures/cufflinksstrict/cufflinksstrict.iso_form.fpkm.tsv")
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  def pearsonScoreTest(tableA: File, tableB: File, minScore: Double = 0.999): Unit = {
    GentrapFunctional.pearsonScoreTest(tableA, tableB, minScore, samples.keySet)
  }
}

object GentrapFunctional {
  def loadMergedCountTable(tableFile: File, sampleName: String): Iterator[Double] = {
    assert(tableFile.exists())
    val lineIter = scala.io.Source.fromFile(tableFile).getLines()
    val header = lineIter.next()
    val sampleIndex = header.stripLineEnd.split("\t").indexOf(sampleName)
    assert(sampleIndex > 0)
    lineIter.map(_.stripLineEnd.split("\t")(sampleIndex).toDouble)
  }

  def pearsonScoreTest(tableA: File, tableB: File, minScore: Double, samples: Set[String]): Unit = {
    samples.foreach { sample =>
      val pValue = pearsonScore(loadMergedCountTable(tableA, sample), loadMergedCountTable(tableB, sample)).getOrElse(0.0)
      assert(pValue >= minScore)
    }
  }
}

/** Functional test for Gentrap with complete options, using gsnap and non_specific strand protocol. */
class GentrapFunctGsnapNonspecificTest extends GentrapFunctional
  with GentrapSuccess
  with Gsnap
  with AllExpressionMeasures
  with Rna1 with Rna2
  with HsapiensGRCh38 {

  def shouldHaveKmerContent = Some(true)
  def paired = true

  override def callVariants = Some(true)

  def strandProtocol = Option("non_specific")
  def annotationRefflat = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.refFlat"))
  def annotationGtf = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.gtf"))
  def ribosomalRefflat = None
}
