package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.{Biopet, SummaryGroup}
import nl.lumc.sasc.biopet.test.Pipeline.cmdConfig
import nl.lumc.sasc.biopet.test.shiva.variantcallers.MuTect2
import nl.lumc.sasc.biopet.test.utils.createTempConfig

class MuTect2Test extends ShivaVariantcallingWgs1 with MuTect2 {

  override def bamFiles =
    super.bamFiles :+ Biopet.fixtureFile("samples", "wgs2", "wgs2.realign.bam")

  override def configs =
    super.configs :+ createTempConfig(
      Map("tumor_normal_pairs" -> List(Map("T" -> "wgs2", "N" -> "wgs1"))))

  override def args =
    super.args ++
      cmdConfig("run_contest", true) ++ cmdConfig(
      "popfile",
      Biopet.fixtureFile("samples", "wgs2", "wgs2.vcf.gz"))

  override def testVariantcallerInfoTag(file: File): Unit = {}

  addSettingsTest(SummaryGroup("shivavariantcalling"),
                  List("somatic_variant_calling"),
                  _ shouldBe true)
  addSettingsTest(SummaryGroup("shivavariantcalling"),
                  List("germline_variant_calling"),
                  _ shouldBe false)

}
