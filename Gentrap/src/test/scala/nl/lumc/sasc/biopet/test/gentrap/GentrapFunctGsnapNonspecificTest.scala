package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import org.testng.annotations.Test

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline, Samples }, Pipeline._
import nl.lumc.sasc.biopet.test.utils.pearsonScore

/** Functional test for Gentrap with complete options, using gsnap and non_specific strand protocol. */
class GentrapFunctGsnapNonspecificTest extends GentrapSuccess with GentrapRefSeq {

  override def functionalTest = true

  // TODO: Map[String, Map[String, Double]] for the count table instead?
  def loadMergedCountTable(tableFile: File): Map[String, Seq[Double]] = {
    val lineIter = scala.io.Source.fromFile(tableFile).getLines()
    val header = lineIter.next()
    val sampleNames = header.stripLineEnd.split("\t").tail.toSeq
    val counts = lineIter.map(_.stripLineEnd.split("\t").tail.toSeq.map(_.toDouble))
    sampleNames.zip(counts.toSeq).toMap
  }

  def samples = Map("rna1" -> List("lib1"))

  override def expressionMeasures = List(
    "fragments_per_gene", "bases_per_gene", "bases_per_exon",
    "cufflinks_strict", "cufflinks_guided", "cufflinks_blind")

  override def strandProtocol = Option("non_specific")

  override def configs = super.configs :+ Samples.rnaMultipleConfig

  override def referenceSpecies = Option("H.sapiens")

  override def referenceName = Option("hg19_mini")

  override def aligner = Option("gsnap")

  override def args = super.args ++
    cmdConfig("dir", Biopet.fixtureFile("gentrap").getAbsolutePath) ++
    cmdConfig("db", "hg19_mini")

  def fragmentsPerGene: File = new File(outputDir, "all_samples.fragments_per_gene")

  @Test def fragmentsPerGeneTest(): Unit = {
    val refFragmentsPerGene = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.fragments_per_gene")

    val tableA = loadMergedCountTable(refFragmentsPerGene)
    val tableB = loadMergedCountTable(fragmentsPerGene)

    tableB.size shouldBe tableA.size
    tableB.keySet shouldEqual tableA.keySet

    val samples = tableB.keySet
    samples.foreach { sample =>
      val tableAValues = tableA(sample).toList
      val tableBValues = tableB(sample).toList
      pearsonScore(tableAValues, tableBValues).getOrElse(0.0) should be >= 0.999
    }
  }
}
