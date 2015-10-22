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
            "R2" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz").getAbsolutePath
          )
        )
      )
    )
  )
  val wgs1Config = createTempConfig(wgs1, "wgs1")

  val wgs1Bam = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "bam" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "wgs1.bam").getAbsolutePath
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
}
