package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.utils._

trait Mgm4459735_3_050Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4459735_3_050.sampleName, "lib1")
  override def configs = super.configs :+ Mgm4459735_3_050.lib1ConfigFile

  override def args = super.args ++ cmdConfig("phred_offset", 33)
}

trait Mgm4459735_3_050Lib2 extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4459735_3_050.sampleName, "lib2")
  override def configs = super.configs :+ Mgm4459735_3_050.lib2ConfigFile

  override def args = super.args ++ cmdConfig("phred_offset", 33)
}

object Mgm4459735_3_050 {
  val sampleName = "Mgm4459735_3_050"
  val lib1ConfigMap = Map("samples" ->
    Map(s"$sampleName" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "mgm4459735.3.050.R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "2cae13e5013a9ed3d832d9b996ae36cd",
            "R2" -> Biopet.fixtureFile("samples", sampleName, "mgm4459735.3.050.R2.fq.gz").getAbsolutePath,
            "R2_md5" -> "f2b539f5c8a967c8028df88c9d010f3e"
          )
        )
      )
    )
  )
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, s"$sampleName-lib1")

  val lib2ConfigMap = Map("samples" ->
    Map(s"$sampleName" ->
      Map("libraries" ->
        Map("lib2" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "mgm4459735.3.050.R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "2cae13e5013a9ed3d832d9b996ae36cd"
          )
        )
      )
    )
  )
  val lib2ConfigFile = createTempConfig(lib2ConfigMap, s"$sampleName-lib2")

}

