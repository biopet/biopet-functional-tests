package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.utils._

trait Mgm4457768_3_050Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4457768_3_050.sampleName, "lib1")
  override def configs = super.configs :+ Mgm4457768_3_050.lib1ConfigFile

  override def args = super.args ++ cmdConfig("phred_offset", 33)
}

trait Mgm4457768_3_050Lib1Uncompressed extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4457768_3_050.sampleName, "lib1")
  override def configs = super.configs :+ Mgm4457768_3_050.lib1UncompressedConfigFile

  override def args = super.args ++ cmdConfig("phred_offset", 33)
}

trait Mgm4457768_3_050Lib2 extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4457768_3_050.sampleName, "lib2")
  override def configs = super.configs :+ Mgm4457768_3_050.lib2ConfigFile

  override def args = super.args ++ cmdConfig("phred_offset", 33)
}

trait Mgm4457768_3_050Lib2Uncompressed extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4457768_3_050.sampleName, "lib2")
  override def configs = super.configs :+ Mgm4457768_3_050.lib2UncompressedConfigFile

  override def args = super.args ++ cmdConfig("phred_offset", 33)
}

object Mgm4457768_3_050 {
  val sampleName = "Mgm4457768_3_050"
  val lib1ConfigMap = Map(
    "samples" ->
      Map(
        s"$sampleName" ->
          Map(
            "libraries" ->
              Map("lib1" ->
                Map(
                  "R1" -> Biopet
                    .fixtureFile("samples", sampleName, "mgm4457768.3.050.R1.fastq.gz")
                    .getAbsolutePath,
                  "R1_md5" -> "2de6a83c2d9fd05c40c67ea46cc5a4fd",
                  "R2" -> Biopet
                    .fixtureFile("samples", sampleName, "mgm4457768.3.050.R2.fastq.gz")
                    .getAbsolutePath,
                  "R2_md5" -> "84331c7c1d59ebc30a023645335c847c"
                )))))
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, s"$sampleName-lib1")

  val lib1UncompressedConfigMap = Map(
    "samples" ->
      Map(
        s"$sampleName" ->
          Map(
            "libraries" ->
              Map("lib1" ->
                Map(
                  "R1" -> Biopet
                    .fixtureFile("samples", sampleName, "mgm4457768.3.050.R1.fastq")
                    .getAbsolutePath,
                  "R1_md5" -> "1275cf63b349ecf7992594310a64c39c",
                  "R2" -> Biopet
                    .fixtureFile("samples", sampleName, "mgm4457768.3.050.R2.fastq")
                    .getAbsolutePath,
                  "R2_md5" -> "b58651f90f68a8c76e8a79b6f17e90fd"
                )))))
  val lib1UncompressedConfigFile = createTempConfig(lib1UncompressedConfigMap, s"$sampleName-lib1")


  val lib2ConfigMap = Map(
    "samples" ->
      Map(
        s"$sampleName" ->
          Map(
            "libraries" ->
              Map(
                "lib2" ->
                  Map(
                    "R1" -> Biopet
                      .fixtureFile("samples", sampleName, "mgm4457768.3.050.R1.fastq.gz")
                      .getAbsolutePath,
                    "R1_md5" -> "2de6a83c2d9fd05c40c67ea46cc5a4fd"
                  )))))
  val lib2ConfigFile = createTempConfig(lib2ConfigMap, s"$sampleName-lib2")

  val lib2UncompressedConfigMap = Map(
    "samples" ->
      Map(
        s"$sampleName" ->
          Map(
            "libraries" ->
              Map(
                "lib2" ->
                  Map(
                    "R1" -> Biopet
                      .fixtureFile("samples", sampleName, "mgm4457768.3.050.R1.fastq")
                      .getAbsolutePath,
                    "R1_md5" -> "1275cf63b349ecf7992594310a64c39c"
                  )))))
  val lib2UncompressedConfigFile = createTempConfig(lib2UncompressedConfigMap, s"$sampleName-lib2")

}
