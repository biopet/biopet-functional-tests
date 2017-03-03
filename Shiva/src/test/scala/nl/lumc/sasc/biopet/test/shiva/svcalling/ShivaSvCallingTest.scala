package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import htsjdk.samtools.util.CloseableIterator
import htsjdk.variant.vcf.VCFFileReader
import htsjdk.variant.variantcontext.VariantContext
import nl.lumc.sasc.biopet.test.Pipeline.cmdConfig
import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }
import org.testng.annotations.Test

trait ShivaSvCallingTest extends Pipeline {

  override def pipelineName = "shivasvcalling"

  def svCallerName: String

  def supportedTypes: List[String]

  override def retries = Option(1)

  override def args = Seq("-BAM", Biopet.fixtureFile("samples/sv/ref_sv-ref_sv.dedup.bam").getAbsolutePath) ++
    cmdConfig("sv_callers", svCallerName) ++
    cmdConfig("reference_fasta", Biopet.fixtureFile("reference/reference.fasta")) ++
    cmdConfig("maxthreads", 1) ++
    cmdConfig("core_memory", 1)

  @Test
  def testSvCaller(): Unit = {
    val resultVcfFileName = s"$svCallerName/ref_sv/ref_sv.$svCallerName.vcf.gz"
    val reader = new VCFFileReader(new File(outputDir, resultVcfFileName), true)

    assertContainsVariant(reader.query("chr1", 2040, 2041), "ITX")
    assertContainsVariant(reader.query("chr1", 4020, 4021), "INS")
    assertContainsVariant(reader.query("chr1", 11400, 12000), "DEL")

    // validating the same translocation, breakdancer and delly differ in how they encode the variant
    assertContainsVariant(reader.query("chr1", 13501, 13620), "CTX")
    assertContainsVariant(reader.query("chrM", 11100, 11101), "TRA")

    assertContainsVariant(reader.query("chrM", 6000, 8000), "INV")

    reader.close()
  }

  def assertContainsVariant(predictedVariants: CloseableIterator[VariantContext], variantType: String): Unit = {
    if (!supportedTypes.contains(variantType)) return

    var variantFound = false
    while (predictedVariants.hasNext) {
      if (variantType == predictedVariants.next.getAttributeAsString("SVTYPE", null))
        variantFound = true
    }
    assert(variantFound, s"$svCallerName did not detect the existing variant (type of the variant: $variantType)")

    predictedVariants.close()
  }

  def devPrint(predictedVariants: CloseableIterator[VariantContext], variantType: String): Unit = {
    if (!supportedTypes.contains(variantType)) return
    println()
    println(s"variants from $svCallerName for $variantType")
    var i = 1
    while (predictedVariants.hasNext) {
      println(s"$i: " + predictedVariants.next)
      i += 1
    }
  }

}

class BreakdancerTest extends ShivaSvCallingTest {

  def svCallerName = "breakdancer"
  def supportedTypes = List("INS", "DEL", "INV", "CTX", "ITX")

}

/*class CleverTest extends ShivaSvCallingTest {

  def svCallerName = "clever"
  // clever detects only insertions and deletions
  def supportedTypes = List("INS", "DEL")

}*/

class DellyTest extends ShivaSvCallingTest {

  def svCallerName = "delly"
  // delly isn't meant for detecting insertions nor intrachromosomal translocations
  def supportedTypes = List("DEL", "INV", "TRA")

}