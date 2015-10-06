package nl.lumc.sasc.biopet.test

import java.io.{ PrintWriter, File }

import org.json4s.jackson.Serialization

/**
 * Created by pjvan_thof on 10/1/15.
 */
object Samples {
  implicit val formats = org.json4s.DefaultFormats

  private def createTempSampleConfig(map: Map[String, Any], name: String): File = {
    val file = File.createTempFile(s"$name.", ".json")
    val writer = new PrintWriter(file)
    writer.println(Serialization.write(map))
    writer.close()
    file.deleteOnExit()
    file
  }

  val wgs1 = Map("samples" ->
    Map("wgs1" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz").getAbsolutePath,
            "R2" -> Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz").getAbsolutePath)
        )
      )
    )
  )
  val wgs1Config = createTempSampleConfig(wgs1, "wgs1")
}
