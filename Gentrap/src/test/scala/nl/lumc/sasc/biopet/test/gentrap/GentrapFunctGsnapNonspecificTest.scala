package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import org.testng.annotations.{ DataProvider, Test }

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline, Samples }, Pipeline._
import nl.lumc.sasc.biopet.test.utils.pearsonScore

trait GentrapFunctional extends Gentrap { this: GentrapAnnotations =>

  override def functionalTest = true

  // TODO: Map[String, Map[String, Double]] for the count table instead?
  def loadMergedCountTable(tableFile: File): Option[Map[String, Seq[Double]]] = {
    if (tableFile.exists) {
      val lineIter = scala.io.Source.fromFile(tableFile).getLines()
      val header = lineIter.next()
      val sampleNames = header.stripLineEnd.split("\t").tail.toSeq
      val counts = lineIter.map(_.stripLineEnd.split("\t").tail.toSeq.map(_.toDouble))
      Option(sampleNames.zip(counts.toSeq).toMap)
    } else None
  }

  def fragmentsPerGene: File = new File(outputDir, "all_samples.fragments_per_gene")

  @DataProvider(name = "mergedExpressionMeasurements")
  def mergedExpressionMeasurementsProvider() = {}

  @Test(dataProvider = "mergedExpressionMeasurements")
  def mergedExpressionMeasurementsTest(fixtureFile: File, pipelineFile: File): Unit = {
    val refFragmentsPerGene = Biopet.fixtureFile("gentrap", "count_table", "func1", "all_samples.fragments_per_gene")

    val maybeTableA = loadMergedCountTable(refFragmentsPerGene)
    val maybeTableB = loadMergedCountTable(fragmentsPerGene)

    (maybeTableA, maybeTableB) match {
      case (None, Some(_)) => fail(s"Can not extract values from required fixture '$refFragmentsPerGene'.")
      case (Some(_), None) => fail(s"Can not extract values from required output '$fragmentsPerGene'.")
      case (None, None)    =>
        fail(s"Can not extract values from required files '$refFragmentsPerGene' and '$fragmentsPerGene'.")
      case (Some(tableA), Some(tableB)) =>

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
}

/** Functional test for Gentrap with complete options, using gsnap and non_specific strand protocol. */
class GentrapFunctGsnapNonspecificTest extends GentrapFunctional with GentrapSuccess with GentrapRefSeq {

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
}
