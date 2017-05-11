package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.utils.ConfigUtils

/** WGS data from The Genome in a Bottle project, it's 2x150 PE data for the NA12878 genome sequenced with Illumina HiSeq 2500, library U0c.*/
trait NA12878WGS extends Samples {

  override def samples = super.samples + ("NA12878" -> NA12878WGS.libraries)

  override def configs = super.configs.::(NA12878WGS.config)

}

object NA12878WGS {

  val config = Biopet.fixtureFile("samples", "NA12878_wgs", "Sample_U0c", "sample_config.json")

  val libraries = ConfigUtils
    .fileToConfigMap(config)
    .get("samples")
    .asInstanceOf[Some[Map[String, Map[String, Map[String, Any]]]]]
    .get
    .get("NA12878")
    .get("libraries")
    .keySet

}
