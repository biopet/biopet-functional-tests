package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.SummaryPipeline
import nl.lumc.sasc.biopet.test.shiva.Shiva
import org.json4s._
import org.testng.annotations.{ Test, DataProvider }

/**
 * Created by pjvan_thof on 10/16/15.
 */
trait ShivaVariantcallingSuccess extends ShivaVariantcalling with SummaryPipeline {
  @DataProvider(name = "variantcallers")
  def variantcallerProvider = Shiva.validVariantcallers.map(Array(_)).toArray

  @Test(dataProvider = "variantcallers", dependsOnGroups = Array("parseSummary"))
  def testVariantcaller(variantcaller: String): Unit = withClue(s"variantcaller: $variantcaller") {
    val dir = new File(outputDir, variantcaller)
    val vcfstats = this.summary \ "shivavariantcalling" \ "stats" \ s"${namePrefix.get}-vcfstats-$variantcaller"
    if (variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      vcfstats shouldBe a[JObject]
    } else {
      assert(!dir.exists())
      vcfstats shouldBe JNothing
    }
  }

  @Test
  def testFinalVariantcallerInfoTag = this.testVariantcallerInfoTag(new File(outputDir, s"$namePrefix.final.vcf.gz"))

}
