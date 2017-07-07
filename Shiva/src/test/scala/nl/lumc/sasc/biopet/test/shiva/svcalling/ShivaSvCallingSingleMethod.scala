package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.shiva.svcallers._
import nl.lumc.sasc.biopet.test.references.TestReference
import org.testng.annotations.{DataProvider, Test}
import scala.collection.JavaConversions._

trait ShivaSvCallingSingleMethod extends ShivaSvCallingSuccess with TestReference {

  def bamFiles = List(Biopet.fixtureFile(s"samples/sv/ref_sv-ref_sv.dedup.bam"))

  @DataProvider(name = "variants")
  def testSvCallers(): Array[Array[Any]] = {
    svCallers.toArray.flatMap(
      caller =>
        Array(
          Array(caller, "chr1", 2040, 2041, "ITX"),
          Array(caller, "chr1", 4003, 4017, "INS"),
          Array(caller, "chr1", 11385, 11988, "DEL"),
          Array(caller, "chr1", 13501, 13620, "CTX"),
          Array(caller, "chr1", 13501, 13620, "CTX"),
          Array(caller, "chrM", 10865, 10866, "BND"),
          Array(caller, "chrM", 6000, 8000, "INV")
      ))
  }

  @Test(dataProvider = "variants")
  def assertContainsVariant(svCaller: SvCaller,
                            chr: String,
                            startPos: Int,
                            endPos: Int,
                            variantType: String): Unit = {
    if (svCaller.supportedTypes.contains(variantType)) {

      val resultVcfFile = new File(outputDir, s"$svCaller/ref_sv/ref_sv.$svCaller.vcf.gz")
      val reader = new VCFFileReader(resultVcfFile, true)

      val predictedVariants = reader.query(chr, startPos, endPos)
      val variantFound = predictedVariants.exists { record =>
        variantType == record.getAttributeAsString("SVTYPE", null)
      }

      assert(
        variantFound,
        s"$svCaller did not detect the existing variant ($chr:$startPos-$endPos, type: $variantType)")

      reader.close()
    }
  }

}

class BreakdancerTest extends ShivaSvCallingSingleMethod {
  def svCallers = List(new Breakdancer)
}

class DellyTest extends ShivaSvCallingSingleMethod {
  def svCallers = List(new Delly)
}

class CleverTest extends ShivaSvCallingSingleMethod {
  def svCallers = List(new Clever)
}
