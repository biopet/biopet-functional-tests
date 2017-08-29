package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.HsapiensGRCh37

trait ToucanHsapiensGRCh37 extends ToucanSuccess with HsapiensGRCh37 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanHsapiensGRCh37Vep86Test extends ToucanHsapiensGRCh37 {
  override def vepVersion = Some("86")
}

class ToucanHsapiensGRCh37Vep88Test extends ToucanHsapiensGRCh37 {
  override def vepVersion = Some("88")
}

class ToucanHsapiensGRCh37Vep90Test extends ToucanHsapiensGRCh37 {
  override def vepVersion = Some("90")
}

class ToucanHsapiensGRCh37ScatterTest extends ToucanHsapiensGRCh37 {
  override def vepVersion = Some("90")
  override def enableScatter = true
}
