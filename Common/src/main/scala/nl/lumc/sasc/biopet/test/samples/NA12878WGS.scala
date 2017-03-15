package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet

/** WGS data from The Genome in a Bottle project, it's 2x150 PE data for the NA12878 genome sequenced with Illumina HiSeq 2500, library U0c.*/
trait NA12878WGS extends Samples {

  //TODO is this necessary: override def samples = addSampleLibrary(super.samples)

  override def configs = super.configs.::(Biopet.fixtureFile("samples", "NA12878_wgs", "Sample_U0c", "tmp.yml"))

  //override def configs = super.configs.::(Biopet.fixtureFile("samples", "NA12878_wgs", "Sample_U0c", "sample_config.yml")) TODO: use this

}
