package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.{SummaryGroup, SummaryPipeline}
import nl.lumc.sasc.biopet.test.shiva.Shiva
import org.testng.annotations.{DataProvider, Test}
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb.Implicts._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by pjvan_thof on 10/16/15.
  */
trait ShivaVariantcallingSuccess extends ShivaVariantcalling with SummaryPipeline {
  @DataProvider(name = "variantcallers")
  def variantcallerProvider: Array[Array[String]] = Shiva.validVariantcallers.map(Array(_)).toArray

  @Test(dataProvider = "variantcallers", dependsOnGroups = Array("summary"))
  def testVariantcaller(variantcaller: String): Unit = withClue(s"variantcaller: $variantcaller") {
    val dir = new File(outputDir, variantcaller)
    val vcfstats = Await.result(
      summaryDb.getStat(runId, pipelineName, s"${namePrefix.get}-vcfstats-$variantcaller"),
      Duration.Inf)
    if (variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      assert(vcfstats.isDefined, s"${namePrefix.get}-vcfstats-$variantcaller not found in summary")
    } else {
      assert(!dir.exists())
      assert(vcfstats.isEmpty,
             s"${namePrefix.get}-vcfstats-$variantcaller found in summary but should not exist")
    }
  }

  @Test
  def testFinalVariantcallerInfoTag(): Unit =
    this.testVariantcallerInfoTag(new File(outputDir, s"${namePrefix.get}.final.vcf.gz"))

  addSettingsTest(SummaryGroup("shivavariantcalling"),
                  List("somatic_variant_calling"),
                  _ shouldBe variantcallers.contains("mutect2"))
  addSettingsTest(SummaryGroup("shivavariantcalling"),
                  List("germline_variant_calling"),
                  _ shouldBe variantcallers.exists(_ != "mutect2"))

}
