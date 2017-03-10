package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

/** WGS data from The Genome in a Bottle project, it's 2x150 PE data for the NA12878 genome sequenced with Illumina HiSeq 2500, library U0c.*/
trait NA12878WGS extends Samples {
  override def samples = addSampleLibrary(super.samples, "NA12878", "GIAB_Illumina")
  override def configs = super.configs :+ NA12878WGS.configFile
}

object NA12878WGS {
  val configMap = Map("samples" ->
    Map("NA12878" ->
      Map("libraries" ->
        Map("GIAB_Illumina" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", "NA12878_wgs", "Sample_U0c", "tmp", "U0c_CAGATC_L002_R1_006.fastq.gz").getAbsolutePath,
            "R1" -> Biopet.fixtureFile("samples", "NA12878_wgs", "Sample_U0c", "tmp", "U0c_CAGATC_L002_R2_006.fastq.gz").getAbsolutePath
          ) //TODO: remove tmp and use the correct file
        )
      )
    )
  )
  val configFile = createTempConfig(configMap, "na12878giabIllumina")

}