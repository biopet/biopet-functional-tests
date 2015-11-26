package nl.lumc.sasc.biopet.test.gentrap

import java.io.File
import scala.collection.mutable.ArrayBuffer

import org.testng.annotations.{ DataProvider, Test }

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.aligners.Gsnap
import nl.lumc.sasc.biopet.test.samples.{ Rna1, Rna2 }
import nl.lumc.sasc.biopet.test.references.HsapiensGRCh38
import nl.lumc.sasc.biopet.test.utils.pearsonScore

trait GentrapFunctional extends Gentrap {

  override def functionalTest = true

  // TODO: Map[String, Map[String, Double]] for the count table instead?
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

  protected def mergedTablePrefix = "all_samples"

  @DataProvider(name = "mergedExpressionMeasurements")
  def mergedExpressionMeasurementsProvider() = {
    val b: ArrayBuffer[Array[Object]] = new ArrayBuffer

    if (this.isInstanceOf[FragmentsPerGene])
      b += Array(s"$mergedTablePrefix.fragments_per_gene")

    if (this.isInstanceOf[BasesPerGene])
      b += Array(s"$mergedTablePrefix.bases_per_gene")

    if (this.isInstanceOf[BasesPerExon])
      b += Array(s"$mergedTablePrefix.bases_per_exon")

    if (this.isInstanceOf[CufflinksStrict]) {
      b += Array(s"$mergedTablePrefix.genes_fpkm_cufflinks_strict")
      b += Array(s"$mergedTablePrefix.isoforms_fpkm_cufflinks_strict")
    }
    // TODO: test also for guided and blind modes. They seem to use some random value that we can't just test for
    //       their Pearson correlations.
    b.toArray
  }

  @Test(dataProvider = "mergedExpressionMeasurements")
  def mergedExpressionMeasurementsTest(fixtureFileName: String): Unit = {
    val fixtureFile = Biopet.fixtureFile("gentrap", "count_table", "func1", fixtureFileName)
    val pipelineFile = new File(outputDir, "expression_estimates" + File.separator + fixtureFile.getName)
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
          pearsonScore(tableA(sample), tableB(sample)).getOrElse(0.0) should be >= 0.999
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

  def strandProtocol = Option("non_specific")
  def annotationRefflat = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.refFlat"))
  def annotationGtf = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.gtf"))
  def annotationExonBed = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.bed"))
  def ribosomalRefflat = None
}
