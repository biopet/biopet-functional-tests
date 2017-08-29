package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.MmusculusGRCm38

trait ToucanMmusculusGRCm38 extends ToucanSuccess with MmusculusGRCm38 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanMmusculusGRCm38Vep86Test extends ToucanMmusculusGRCm38 {
  override def vepVersion = Some("86")
}

class ToucanMmusculusGRCm38Vep88Test extends ToucanMmusculusGRCm38 {
  override def vepVersion = Some("88")
}

class ToucanMmusculusGRCm38Vep90Test extends ToucanMmusculusGRCm38 {
  override def vepVersion = Some("90")
}

class ToucanMmusculusGRCm38ScatterTest extends ToucanMmusculusGRCm38 {
  override def vepVersion = Some("90")
  override def enableScatter = true
}
