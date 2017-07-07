package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.shiva.svcallers._
import org.testng.annotations.Test

import scala.collection.JavaConversions._

class ShivaSvCallingTest extends ShivaSvCallingSingleMethod {

  def svCallers = List(new Breakdancer, new Delly)

  override def bamFiles = super.bamFiles.::(Biopet.fixtureFile(s"samples/wgs1/wgs1.bam"))

  @Test
  def testMergedResult(): Unit = {
    val reader = new VCFFileReader(new File(outputDir, "ref_sv.merged.vcf"), false)

    val expectedVariants = List(
      ("chr1", 11400, 12000, "DEL"),
      /**("chr1", 13501, 13620, "TRA"),*/
      ("chrM", 6000, 8000, "INV")
    )

    for (expected <- expectedVariants) {
      val variantFound = reader.exists(
        predicted =>
          expected._1 == predicted.getContig &&
            expected._4 == predicted.getAttributeAsString("SVTYPE", null) && (
            expected._2 <= predicted.getStart &&
              expected._3 > predicted.getStart ||
              expected._2 > predicted.getStart &&
                expected._2 < predicted.getEnd
        ))

      assert(
        variantFound,
        s"Expected variant is missing from the merged result (${expected._1}:${expected._2}-${expected._3}, type: ${expected._4})")
    }

    reader.close()
  }

  @Test(dataProvider = "callers")
  def assertNoVariantsFound(svCaller: SvCaller): Unit = {
    val resultVcfFile = new File(outputDir, s"$svCaller/wgs1/wgs1.$svCaller.vcf.gz")
    if (resultVcfFile.exists) {
      val reader = new VCFFileReader(resultVcfFile, false)
      assert(!reader.nonEmpty,
             s"$svCaller finds structural variants from a sample that doesn't have any.")
      reader.close()
    }
  }

}
