package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.shiva.svcallers._
import org.testng.annotations.Test

import scala.collection.JavaConverters._

class ShivaSvCallingTest extends ShivaSvCallingSingleMethod {

  def svCallers = List(new Breakdancer, new Delly)

  override def bamFiles = super.bamFiles.::(Biopet.fixtureFile(s"samples/wgs1/wgs1.bam"))

  @Test
  def testMergedResult(): Unit = {
    val iterator = new VCFFileReader(new File(outputDir, "ref_sv.merged.vcf"), false).iterator()
    val predictedVariants = iterator.asScala.toList

    val expectedVariants = List(("chr1", 2390, 2400, "ITX"), // delly's duplication has shifted the variant a bit to the right
      ("chr1", 11400, 12000, "DEL"), ("chr1", 13501, 13620, "TRA"), ("chrM", 6000, 8000, "INV"))

    for (expected <- expectedVariants) {
      val variantFound = predictedVariants.exists(predicted =>
        expected._1 == predicted.getContig && expected._4 == predicted.getAttributeAsString("SVTYPE", null) &&
          (expected._2 <= predicted.getStart && expected._3 > predicted.getStart || expected._2 > predicted.getStart && expected._2 < predicted.getEnd))

      assert(variantFound, s"Expected variant is missing from the merged result (${expected._1}:${expected._2}-${expected._3}, type: ${expected._4})")
    }

    iterator.close()
  }

  @Test
  def testReferenceDataHandling(svCaller: SvCaller): Unit = {
    svCallers.foreach(assertNoVariantsFound(_))
  }

  def assertNoVariantsFound(svCaller: SvCaller): Unit = {
    val resultVcfFile = new File(outputDir, s"$svCaller/wgs1/wgs1.$svCaller.vcf.gz")
    if (resultVcfFile.exists) {
      val iterator = new VCFFileReader(resultVcfFile, false).iterator
      val variantsFound = iterator.hasNext
      assert(!variantsFound, s"$svCaller finds structural variants from a sample that doesn't have any.")
      iterator.close()
    }
  }

}