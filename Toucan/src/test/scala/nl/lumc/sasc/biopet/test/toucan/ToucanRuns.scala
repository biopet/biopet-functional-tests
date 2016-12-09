package nl.lumc.sasc.biopet.test.toucan

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references._

/**
 * Created by pjvan_thof on 11/17/15.
 */
class ToucanHsapiensHg19Test extends ToucanSuccess with HsapiensHg19 {
  override def vepConfig = Some("86")
  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanHsapiensGRCh37Test extends ToucanSuccess with HsapiensGRCh37 {
  override def vepConfig = Some("86")
  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanHsapiensGRCh37ScatterTest extends ToucanSuccess with HsapiensGRCh37 {
  override def vepConfig = Some("86")
  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
  override def enableScatter = true
}

class ToucanHsapiensGRCh38Test extends ToucanSuccess with HsapiensGRCh38 {
  override def vepConfig = Some("86")
  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanMmusculusGRCm38Test extends ToucanSuccess with MmusculusGRCm38 {
  override def vepConfig = Some("86")
  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanRnorvegicusRnor_6Test extends ToucanSuccess with RnorvegicusRnor_6 {
  override def vepConfig = Some("86")
  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanCelegansWBcel235Test extends ToucanSuccess with CelegansWBcel235 {
  override def vepConfig = Some("86")
  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}
