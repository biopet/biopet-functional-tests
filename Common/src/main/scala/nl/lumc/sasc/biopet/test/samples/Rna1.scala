package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

trait Rna1 extends Samples with Rna1Lib1 with Rna1Lib2

trait Rna1Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Rna1.sampleName, "lib1")
  override def configs = super.configs :+ Rna1.lib1ConfigFile
}

trait Rna1Lib2 extends Samples {
  override def samples = addSampleLibrary(super.samples, Rna1.sampleName, "lib2")
  override def configs = super.configs :+ Rna1.lib2ConfigFile
}

object Rna1 {
  val sampleName = "rna1"
  val lib1ConfigMap = Map("samples" ->
    Map("rna1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "S17_L1_1.fq.gz").getAbsolutePath,
            "R1_md5" -> "c91000229c5847ee794d715b632c4d58",
            "R2" -> Biopet.fixtureFile("samples", sampleName, "S17_L1_2.fq.gz").getAbsolutePath,
            "R2_md5" -> "504bd3a1ba828ce0485cd6b2a7ed2051"
          )
        )
      )
    )
  )
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, "rna1-lib1")

  val lib2ConfigMap = Map("samples" ->
    Map("rna1" ->
      Map("libraries" ->
        Map("lib2" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", "rna1", "S27_L1_1.fq.gz").getAbsolutePath,
            "R1_md5" -> "6e35b3f17daefec92cd439423bc3d3d8",
            "R2" -> Biopet.fixtureFile("samples", "rna1", "S27_L1_2.fq.gz").getAbsolutePath,
            "R2_md5" -> "f2b34fc0b9a0157741a37eb5c81ce0ba"
          )
        )
      )
    )
  )
  val lib2ConfigFile = createTempConfig(lib2ConfigMap, s"$sampleName-lib2")
}

