package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.shiva.variantcallers.{MuTect2, Unifiedgenotyper}
import nl.lumc.sasc.biopet.test.utils.createTempConfig

trait ShivaVariantcallingMutect2
    extends ShivaVariantcallingWgs1
    with MuTect2
    with Unifiedgenotyper {
  override def bamFiles: List[File] =
    Biopet.fixtureFile("samples", "wgs2", "wgs2.realign.bam") :: super.bamFiles

  override def configs: List[File] =
    super.configs :+ createTempConfig(
      Map("samples" -> Map("wgs2" -> Map("tags" -> Map("type" -> "tumor", "normal" -> "wgs1")))))
}

class MuTect2Test extends ShivaVariantcallingMutect2

class MuTect2ConTestest extends ShivaVariantcallingMutect2 {
  override def runContest = Some(true)
  override def popFile = Some(Biopet.fixtureFile("samples", "wgs2", "wgs2.vcf.gz"))
}
