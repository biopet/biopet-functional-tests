package nl.lumc.sasc.biopet.test

import java.io.File

/**
 * Created by pjvan_thof on 10/1/15.
 */
object Samples {
  val wgs1 = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"),
            "R2" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))
        )
      )
    )
  )
}
