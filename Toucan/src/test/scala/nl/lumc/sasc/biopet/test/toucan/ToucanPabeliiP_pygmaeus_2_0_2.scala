package nl.lumc.sasc.biopet.test.toucan

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.PabeliiP_pygmaeus_2_0_2

trait ToucanPabeliiP_pygmaeus_2_0_2 extends ToucanSuccess with PabeliiP_pygmaeus_2_0_2 {
  override def inputVcf =
    Some(Biopet.fixtureFile("toucan", "two_vars_each_chrom_human.vcf.gz"))
  override def functionalTest = false
}

class ToucanPabeliiP_pygmaeus_2_0_2Vep86Test extends ToucanPabeliiP_pygmaeus_2_0_2 {
  override def vepVersion = Some("86")
}

class ToucanPabeliiP_pygmaeus_2_0_2Vep88Test extends ToucanPabeliiP_pygmaeus_2_0_2 {
  override def vepVersion = Some("88")
}

class ToucanPabeliiP_pygmaeus_2_0_2Vep90Test extends ToucanPabeliiP_pygmaeus_2_0_2 {
  override def vepVersion = Some("90")
}
