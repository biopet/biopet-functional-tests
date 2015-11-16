package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

trait Rna2 extends Samples with Rna2Lib1

trait Rna2Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Rna2.sampleName, "lib1")
  override def configs = super.configs :+ Rna2.lib1ConfigFile
}

object Rna2 {
  val sampleName = "rna2"
  val lib1ConfigMap = Map("samples" ->
    Map("rna1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "S18_L1_1.fq.gz").getAbsolutePath,
            "R1_md5" -> "31abffce26743b77c7ba7a9d1b49ed13",
            "R2" -> Biopet.fixtureFile("samples", sampleName, "S18_L1_2.fq.gz").getAbsolutePath,
            "R2_md5" -> "9ac9f9ebf46ec6188aaf2985d2bf9bde"
          )
        )
      )
    )
  )
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, "rna1-lib1")
}


