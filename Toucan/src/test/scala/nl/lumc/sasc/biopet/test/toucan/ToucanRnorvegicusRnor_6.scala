package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.RnorvegicusRnor_6

trait ToucanRnorvegicusRnor_6 extends ToucanSuccess with RnorvegicusRnor_6 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanRnorvegicusRnor_6Vep86Test extends ToucanRnorvegicusRnor_6 {
  override def vepVersion = Some("86")
}

class ToucanRnorvegicusRnor_6Vep88Test extends ToucanRnorvegicusRnor_6 {
  override def vepVersion = Some("88")
}

class ToucanRnorvegicusRnor_6Vep90Test extends ToucanRnorvegicusRnor_6 {
  override def vepVersion = Some("90")
}

class ToucanRnorvegicusRnor_6ScatterTest extends ToucanRnorvegicusRnor_6 {
  override def vepVersion = Some("90")
  override def enableScatter = true
}
