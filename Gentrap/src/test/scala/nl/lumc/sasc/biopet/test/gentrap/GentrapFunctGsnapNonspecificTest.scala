package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import org.testng.annotations.Test

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
      val pipelineFile = new File(outputDir, "expresion_measures/fragmentspergene/fragmentspergene.fragments_per_gene.tsv")
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  @Test
  def testBaseCounts(): Unit = {
    if (this.isInstanceOf[BaseCounts]) {
      val fixtureFile = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.bases_per_gene")
      val pipelineFile = new File(outputDir, "expresion_measures/basecounts/basecounts.nonStrandedMetaExonCounts.tsv")
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  @Test
  def testCufflinksStrictGenes(): Unit = {
    if (this.isInstanceOf[CufflinksStrict]) {
      val fixtureFile = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.genes_fpkm_cufflinks_strict")
      val pipelineFile = new File(outputDir, "expresion_measures/cufflinksstrict/cufflinksstrict.genes.fpkm.tsv")
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  @Test
  def testCufflinksStrictIsoform(): Unit = {
    if (this.isInstanceOf[CufflinksStrict]) {
      val fixtureFile = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.isoforms_fpkm_cufflinks_strict")
      val pipelineFile = new File(outputDir, "expresion_measures/cufflinksstrict/cufflinksstrict.iso_form.fpkm.tsv")
      pearsonScoreTest(fixtureFile, pipelineFile)
    }
  }

  def loadMergedCountTable(tableFile: File): Option[Map[String, List[Double]]] = {
    if (tableFile.exists) {
      val lineIter = scala.io.Source.fromFile(tableFile).getLines()
      val header = lineIter.next()
      val sampleNames = header.stripLineEnd.split("\t").tail.toVector
      val counts = lineIter
        .map(_.stripLineEnd.split("\t").tail.toVector.map(_.toDouble))
        .toList
      val res = sampleNames.zipWithIndex
        .map(_._2)
        .map(idx => (sampleNames(idx), counts.map(row => row(idx))))
      Option(res.toMap)
    } else None
  }

  def pearsonScoreTest(tableA: File, tableB: File, minScore: Double = 0.999): Unit = {
    (loadMergedCountTable(tableA), loadMergedCountTable(tableB)) match {
      case (None, Some(_)) => fail(s"Can not extract values from required fixture '$tableA'.")
      case (Some(_), None) => fail(s"Can not extract values from required output '$tableB'.")
      case (None, None) =>
        fail(s"Can not extract values from required files '$tableA' and '$tableB'.")
      case (Some(a), Some(b)) =>

        b.size shouldBe a.size
        b.keySet shouldEqual a.keySet

        val samples = b.keySet
        samples.foreach { sample =>
          pearsonScore(a(sample), b(sample)).getOrElse(0.0) should be >= minScore
        }
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

  def paired = true

  def strandProtocol = Option("non_specific")
  def annotationRefflat = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.refFlat"))
  def annotationGtf = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.gtf"))
  def ribosomalRefflat = None
}
