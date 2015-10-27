package nl.lumc.sasc.biopet.test

import java.io.File

import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 10/1/15.
 */
object Samples {
  val wgs1 = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "b859d6dd76a6861ce7e9a978ae2e530e",
            "R2" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz").getAbsolutePath,
            "R2_md5" -> "986acc7bda0bf2ef55c52431f54fe3a9"
          )
        )
      )
    )
  )
  val wgs1Config = createTempConfig(wgs1, "wgs1")

  val wgs2 = Map("samples" ->
    Map("wgs2" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples" + File.separator + "wgs2" + File.separator + "wgs2-lib1_R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "02f9197c80d6a249e8b06173b1a24c07",
            "R2" -> Biopet.fixtureFile("samples" + File.separator + "wgs2" + File.separator + "wgs2-lib1_R2.fq.gz").getAbsolutePath,
            "R2_md5" -> "cffeb566425da8080dc8b425e5c9e6f9"
          ), "lib2" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples" + File.separator + "wgs2" + File.separator + "wgs2-lib2_R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "54d2e6c92ee8899dd2432a8c66124f29",
            "R2" -> Biopet.fixtureFile("samples" + File.separator + "wgs2" + File.separator + "wgs2-lib2_R2.fq.gz").getAbsolutePath,
            "R2_md5" -> "c8a491e3d587646dcac5a201a2520e47"
          )
        )
      )
    )
  )
  val wgs2Config = createTempConfig(wgs2, "wgs2")

  val wgs1Bam = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "bam" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "wgs1.bam").getAbsolutePath,
            "bam_md5" -> "f69ad453cc62c703d394188120fe03fd"
          )
        )
      )
    )
  )
  val wgs1BamConfig = createTempConfig(wgs1Bam, "wgs1Bam")

  val wgs1BamWrongHeader = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "bam" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "wgs1_wrongheader.bam").getAbsolutePath
          )
        )
      )
    )
  )
  val wgs1BamWrongHeaderConfig = createTempConfig(wgs1BamWrongHeader, "wgs1Bam")

  val na12878Gatc30x = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples" + File.separator + "NA12878" + File.separator + "gcat_set_025_1.fastq.gz").getAbsolutePath,
            "R2" -> Biopet.fixtureFile("samples" + File.separator + "NA12878" + File.separator + "gcat_set_025_2.fastq.gz").getAbsolutePath
          )
        )
      )
    )
  )
  val na12878Gatc30xConfig = createTempConfig(na12878Gatc30x, "na12878Gatc30x")
}
