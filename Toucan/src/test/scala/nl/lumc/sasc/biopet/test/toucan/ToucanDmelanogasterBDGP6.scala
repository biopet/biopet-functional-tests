package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.DmelanogasterBDGP6

trait ToucanDmelanogasterBDGP6 extends ToucanSuccess with DmelanogasterBDGP6 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanDmelanogasterBDGP6Vep86Test extends ToucanDmelanogasterBDGP6 {
  override def vepVersion = Some("86")
}

class ToucanDmelanogasterBDGP6Vep88Test extends ToucanDmelanogasterBDGP6 {
  override def vepVersion = Some("88")
}

class ToucanDmelanogasterBDGP6Vep90Test extends ToucanDmelanogasterBDGP6 {
  override def vepVersion = Some("90")
}
