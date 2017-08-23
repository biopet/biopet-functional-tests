package nl.lumc.sasc.biopet.test.downloadgenomes

import java.io.File

import nl.lumc.sasc.biopet.test.{Biopet, Pipeline}
import nl.lumc.sasc.biopet.test.Pipeline._
import org.testng.SkipException
import org.testng.annotations.DataProvider

/**
  * Created by pjvan_thof on 13-5-16.
  */
trait ValidateAnnotations extends Pipeline {

  val genomes: Map[String, List[String]] = {
    if (Biopet.speciesDir.isEmpty)
      throw new SkipException("species.dir is not defined, skipping this test")
    Biopet.speciesDir.get
      .list()
      .filter(!_.startsWith("."))
      .map { species =>
        species -> new File(Biopet.speciesDir.get, species)
          .list()
          .filter(!_.startsWith("."))
          .toList
      }
      .toMap
  }

  val speciesDir: File = Biopet.speciesDir.get

  def pipelineName = "validateannotations"

  override def args: Seq[String] =
    super.args ++ cmdArg("--speciesdir", Biopet.speciesDir)

  @DataProvider(name = "genomesProvider")
  def genomesProvider: Array[Array[String]] = {
    genomes.toArray.flatMap(s => s._2.map(g => Array(s._1, g)))
  }

}
