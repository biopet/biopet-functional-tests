package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

trait Mgm4457768_3_050Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4457768_3_050.sampleName, "lib1")
  override def configs = super.configs :+ Mgm4457768_3_050.lib1ConfigFile
}

trait Mgm4457768_3_050Lib2 extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4457768_3_050.sampleName, "lib2")
  override def configs = super.configs :+ Mgm4457768_3_050.lib2ConfigFile
}

object Mgm4457768_3_050 {
  val sampleName = "Mgm4457768_3_050"
  val lib1ConfigMap = Map("samples" ->
    Map(s"$sampleName" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "mgm4457768.3.050.R1.fastq.gz").getAbsolutePath,
            "R1_md5" -> "2de6a83c2d9fd05c40c67ea46cc5a4fd",
            "R2" -> Biopet.fixtureFile("samples", sampleName, "mgm4457768.3.050.R2.fastq.gz").getAbsolutePath,
            "R2_md5" -> "84331c7c1d59ebc30a023645335c847c"
          )
        )
      )
    )
  )
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, s"$sampleName-lib1")

  val lib2ConfigMap = Map("samples" ->
    Map(s"$sampleName" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "mgm4457768.3.050.R1.fastq.gz").getAbsolutePath,
            "R1_md5" -> "2de6a83c2d9fd05c40c67ea46cc5a4fd"
          )
        )
      )
    )
  )
  val lib2ConfigFile = createTempConfig(lib2ConfigMap, s"$sampleName-lib2")

}

