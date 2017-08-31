package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.DrerioGRCz10

trait ToucanDrerioGRCz10 extends ToucanSuccess with DrerioGRCz10 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanDrerioGRCz10Vep86Test extends ToucanDrerioGRCz10 {
  override def vepVersion = Some("86")
}

class ToucanDrerioGRCz10Vep88Test extends ToucanDrerioGRCz10 {
  override def vepVersion = Some("88")
}

class ToucanDrerioGRCz10Vep90Test extends ToucanDrerioGRCz10 {
  override def vepVersion = Some("90")
}
