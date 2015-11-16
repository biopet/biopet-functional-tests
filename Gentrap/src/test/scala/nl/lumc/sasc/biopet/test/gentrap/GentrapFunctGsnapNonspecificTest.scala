package nl.lumc.sasc.biopet.test.gentrap

import java.io.File
import scala.collection.mutable.ArrayBuffer

import org.testng.annotations.{ DataProvider, Test }

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.{ HsapiensGRCh38, Reference }
import nl.lumc.sasc.biopet.test.utils.pearsonScore

trait GentrapFunctional extends Gentrap { this: GentrapAnnotations with ExpressionMeasures with Reference =>

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

  protected def mergedTablePrefix = "all_sample"

  @DataProvider(name = "mergedExpressionMeasurements")
  def mergedExpressionMeasurementsProvider() = {
    val b: ArrayBuffer[Array[Object]] = new ArrayBuffer
    if (expressionMeasures.contains("fragments_per_gene"))
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.fragments_per_gene"))
    if (expressionMeasures.contains("bases_per_gene"))
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.bases_per_gene"))
    if (expressionMeasures.contains("bases_per_exon"))
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.bases_per_exon"))
    if (expressionMeasures.contains("cufflinks_strict")) {
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.genes_fpkm_cufflinks_strict"))
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.isoforms_fpkm_cufflinks_strict"))
    }
    if (expressionMeasures.contains("cufflinks_guided")) {
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.genes_fpkm_cufflinks_guided"))
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.isoforms_fpkm_cufflinks_guided"))
    }
    if (expressionMeasures.contains("cufflinks_blind")) {
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.genes_fpkm_cufflinks_blind"))
      b += Array(Biopet.fixtureFile("gentrap", "count_table", "func1", s"$mergedTablePrefix.isoforms_fpkm_cufflinks_blind"))
    }
    b.toArray
  }

  @Test(dataProvider = "mergedExpressionMeasurements")
  def mergedExpressionMeasurementsTest(fixtureFile: File, pipelineFile: File): Unit = {
    val maybeTableA = loadMergedCountTable(fixtureFile)
    val maybeTableB = loadMergedCountTable(pipelineFile)

    (maybeTableA, maybeTableB) match {
      case (None, Some(_)) => fail(s"Can not extract values from required fixture '$fixtureFile'.")
      case (Some(_), None) => fail(s"Can not extract values from required output '$pipelineFile'.")
      case (None, None) =>
        fail(s"Can not extract values from required files '$fixtureFile' and '$pipelineFile'.")
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
class GentrapFunctGsnapNonspecificTest extends GentrapFunctional
  with GentrapSuccess
  with GentrapRefSeq
  with AllExpressionMeasures
  with HsapiensGRCh38 {

  override def strandProtocol = Option("non_specific")
  override def aligner = Option("gsnap")
}
