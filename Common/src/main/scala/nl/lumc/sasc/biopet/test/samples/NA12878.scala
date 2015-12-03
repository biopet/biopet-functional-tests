package nl.lumc.sasc.biopet.test.samples

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 11/16/15.
 */
trait NA12878 extends Samples with NA12878Bioplanet30x

trait NA12878Bioplanet30x extends Samples {
  override def samples = addSampleLibrary(super.samples, "NA12878", "biopetplanet-30x")
  override def configs = super.configs :+ NA12878.bioplanet30xConfigFile
}

object NA12878 {
  val bioplanet30xConfigMap = Map("samples" ->
    Map("NA12878" ->
      Map("libraries" ->
        Map("biopetplanet-30x" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", "NA12878", "biopetplanet-30x", "gcat_set_025_1.fastq.gz").getAbsolutePath,
            "R2" -> Biopet.fixtureFile("samples", "NA12878", "biopetplanet-30x", "gcat_set_025_2.fastq.gz").getAbsolutePath
          )
        )
      )
    )
  )
  val bioplanet30xConfigFile = createTempConfig(bioplanet30xConfigMap, "na12878Gatc30x")

}