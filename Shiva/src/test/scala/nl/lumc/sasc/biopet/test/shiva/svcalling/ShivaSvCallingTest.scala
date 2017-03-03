package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Pipeline.cmdConfig
import nl.lumc.sasc.biopet.test.shiva.svcallers._
import nl.lumc.sasc.biopet.test.Biopet
import org.testng.annotations.Test

trait ShivaSvCallingTest extends ShivaSvCalling {

  override def retries = Option(1)

  override def args = super.args ++ Seq("-BAM", Biopet.fixtureFile("samples/sv/ref_sv-ref_sv.dedup.bam").getAbsolutePath) ++
    cmdConfig("reference_fasta", Biopet.fixtureFile("reference/reference.fasta")) ++
    cmdConfig("maxthreads", 1) ++
    cmdConfig("core_memory", 2)

  @Test
  def testSvCaller(): Unit = {
    val resultVcfFileName = s"${svCaller.svCallerName}/ref_sv/ref_sv.${svCaller.svCallerName}.vcf.gz"
    val reader = new VCFFileReader(new File(outputDir, resultVcfFileName), true)

    assertContainsVariant(reader, "chr1", 2040, 2041, "ITX")
    assertContainsVariant(reader, "chr1", 4020, 4021, "INS")
    assertContainsVariant(reader, "chr1", 11400, 12000, "DEL")

    // validating the same translocation, breakdancer and delly differ in how they encode the variant
    assertContainsVariant(reader, "chr1", 13501, 13620, "CTX")
    assertContainsVariant(reader, "chrM", 11100, 11101, "TRA")

    assertContainsVariant(reader, "chrM", 6000, 8000, "INV")

    reader.close()
  }

  def assertContainsVariant(vcfFileReader: VCFFileReader, chr: String, startPos: Int, endPos: Int, variantType: String): Unit = {
    if (!svCaller.supportedTypes.contains(variantType)) return

    val predictedVariants = vcfFileReader.query(chr, startPos, endPos)

    var variantFound = false
    while (predictedVariants.hasNext) {
      if (variantType == predictedVariants.next.getAttributeAsString("SVTYPE", null))
        variantFound = true
    }

    assert(variantFound, s"${svCaller.svCallerName} did not detect the existing variant ($chr:$startPos-$endPos, type: $variantType)")

    predictedVariants.close()
  }

}

class BreakdancerTest extends ShivaSvCallingTest {
  def svCaller = new Breakdancer
}

/*class CleverTest extends ShivaSvCallingTest {
  def svCaller = new Clever
}*/

class DellyTest extends ShivaSvCallingTest {
  def svCaller = new Delly
}