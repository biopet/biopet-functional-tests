package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.HsapiensGRCh38

trait ToucanHsapiensGRCh38 extends ToucanSuccess with HsapiensGRCh38 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanHsapiensGRCh38Vep86Test extends ToucanHsapiensGRCh38 {
  override def vepVersion = Some("86")
}

class ToucanHsapiensGRCh38Vep88Test extends ToucanHsapiensGRCh38 {
  override def vepVersion = Some("88")
}
