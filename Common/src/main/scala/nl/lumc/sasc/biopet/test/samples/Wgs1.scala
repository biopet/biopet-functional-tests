package nl.lumc.sasc.biopet.test.samples

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 11/16/15.
 */
trait Wgs1 extends Samples {
  override def samples = addSampleLibrary(super.samples, "wgs1", "lib1")
  override def configs = super.configs :+ Wgs1.lib1ConfigFile
}

trait Wgs1SingleEnd extends Samples {
  override def samples = addSampleLibrary(super.samples, "wgs1", "lib1")
  override def configs = super.configs :+ Wgs1.lib2ConfigFile
}

trait Wgs1Bam extends Samples {
  override def samples = addSampleLibrary(super.samples, "wgs1", "lib1")
  override def configs = super.configs :+ Wgs1.bamConfigFile
}

trait Wgs1WrongBam extends Samples {
  override def samples = addSampleLibrary(super.samples, "wgs1", "wrongBam")
  override def configs = super.configs :+ Wgs1.wrongBamConfigFile
}

object Wgs1 {
  // Paired-End
  val lib1ConfigMap = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", "wgs1", "R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "b859d6dd76a6861ce7e9a978ae2e530e",
            "R2" -> Biopet.fixtureFile("samples", "wgs1", "R2.fq.gz").getAbsolutePath,
            "R2_md5" -> "986acc7bda0bf2ef55c52431f54fe3a9"
          )
        )
      )
    )
  )
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, "wgs1")

  // Single-End
  val lib2ConfigMap = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", "wgs1", "R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "b859d6dd76a6861ce7e9a978ae2e530e"
          )
        )
      )
    )
  )
  val lib2ConfigFile = createTempConfig(lib2ConfigMap, "wgs1")

  val bamConfigMap = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "bam" -> Biopet.fixtureFile("samples", "wgs1", "wgs1.bam").getAbsolutePath,
            "bam_md5" -> "688b8d24b388e24b09ad9239dfe387fb"
          )
        )
      )
    )
  )
  val bamConfigFile = createTempConfig(bamConfigMap, "wgs1Bam")

  val wrongBamConfigMap = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("wrongBam" ->
          Map(
            "bam" -> Biopet.fixtureFile("samples", "wgs1", "wgs1_wrongheader.bam").getAbsolutePath
          )
        )
      )
    )
  )
  val wrongBamConfigFile = createTempConfig(wrongBamConfigMap, "wgs1Bam")
}
