package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.HsapiensHg19

trait ToucanHsapiensHg19 extends ToucanSuccess with HsapiensHg19 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanHsapiensHg19Vep86Test extends ToucanHsapiensHg19 {
  override def vepVersion = Some("86")
}

class ToucanHsapiensHg19Vep88Test extends ToucanHsapiensHg19 {
  override def vepVersion = Some("88")
}

class ToucanHsapiensHg19Vep90Test extends ToucanHsapiensHg19 {
  override def vepVersion = Some("90")
}
