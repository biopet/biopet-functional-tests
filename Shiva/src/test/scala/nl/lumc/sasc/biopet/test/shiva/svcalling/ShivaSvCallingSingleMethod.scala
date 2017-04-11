package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.shiva.svcallers._
import nl.lumc.sasc.biopet.test.references.TestReference
import org.testng.annotations.Test

trait ShivaSvCallingSingleMethod extends ShivaSvCallingSuccess with TestReference {

  override def retries = Option(1)

  def bamFiles = List(Biopet.fixtureFile(s"samples/sv/ref_sv-ref_sv.dedup.bam"))

  @Test
  def testSvCallers(): Unit = {
    svCallers.foreach(testSvCaller(_))
  }

  def testSvCaller(svCaller: SvCaller): Unit = {
    val resultVcfFileName = s"$svCaller/ref_sv/ref_sv.$svCaller.vcf.gz"
    val reader = new VCFFileReader(new File(outputDir, resultVcfFileName), true)

    assertContainsVariant(svCaller, reader, "chr1", 2040, 2041, "ITX")
    assertContainsVariant(svCaller, reader, "chr1", 4020, 4021, "INS")
    assertContainsVariant(svCaller, reader, "chr1", 11400, 12000, "DEL")

    // validating the same translocation, breakdancer and delly differ in how they encode the variant
    assertContainsVariant(svCaller, reader, "chr1", 13501, 13620, "CTX")
    assertContainsVariant(svCaller, reader, "chrM", 11100, 11101, "TRA")

    assertContainsVariant(svCaller, reader, "chrM", 6000, 8000, "INV")

    reader.close()
  }

  def assertContainsVariant(svCaller: SvCaller, vcfFileReader: VCFFileReader, chr: String, startPos: Int, endPos: Int, variantType: String): Unit = {
    if (!svCaller.supportedTypes.contains(variantType)) return

    val predictedVariants = vcfFileReader.query(chr, startPos, endPos)

    var variantFound = false
    while (predictedVariants.hasNext) {
      if (variantType == predictedVariants.next.getAttributeAsString("SVTYPE", null))
        variantFound = true
    }

    assert(variantFound, s"$svCaller did not detect the existing variant ($chr:$startPos-$endPos, type: $variantType)")

    predictedVariants.close()
  }

}

class BreakdancerTest extends ShivaSvCallingSingleMethod {
  def svCallers = List(new Breakdancer)
}

/*class CleverTest extends ShivaSvCallingTest {
  def svCaller = List(new Clever)
}*/

class DellyTest extends ShivaSvCallingSingleMethod {
  def svCallers = List(new Delly)
}

class CleverTest extends ShivaSvCallingSingleMethod {
  def svCallers = List(new Clever)
}