package nl.lumc.sasc.biopet.test.shiva.variantcalling

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.shiva.variantcallers.MuTect2
import nl.lumc.sasc.biopet.test.utils.createTempConfig

class MuTect2Test extends ShivaVariantcallingWgs1 with MuTect2 {

  override def bamFiles = super.bamFiles :+ Biopet.fixtureFile("samples", "wgs2", "wgs2.realign.bam")

  override def configs = super.configs :+ createTempConfig(Map("tumor_normal_pairs" -> List(Map("T" -> "wgs2", "N" -> "wgs1"))))

}