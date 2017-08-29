package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.CelegansWBcel235

trait ToucanCelegansWBcel235 extends ToucanSuccess with CelegansWBcel235 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanCelegansWBcel235Vep86Test extends ToucanCelegansWBcel235 {
  override def vepVersion = Some("86")
}

class ToucanCelegansWBcel235Vep88Test extends ToucanCelegansWBcel235 {
  override def vepVersion = Some("88")
}

class ToucanCelegansWBcel235Vep90Test extends ToucanCelegansWBcel235 {
  override def vepVersion = Some("90")
}

class ToucanCelegansWBcel235ScatterTest extends ToucanCelegansWBcel235 {
  override def vepVersion = Some("90")
  override def enableScatter = true
}
