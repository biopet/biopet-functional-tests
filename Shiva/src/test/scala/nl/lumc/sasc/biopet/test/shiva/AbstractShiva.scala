package nl.lumc.sasc.biopet.test.shiva

import java.io.{PrintWriter, File}

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.utils.ConfigUtils

/**
 * Created by pjvan_thof on 5/26/15.
 */
abstract class AbstractShiva extends Pipeline {

  def pipelineName = "shiva"

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def summaryFile = new File(outputDir, s"Shiva.summary.json")

  def args = {
    val sampleFile = File.createTempFile("samples.", ".json")
    sampleFile.deleteOnExit()
    val writer = new PrintWriter(sampleFile)
    writer.print(ConfigUtils.mapToJson(sampleConfig).spaces2)
    writer.close()
    Seq("-cv", s"output_dir=$outputDir", "-config", sampleFile.getAbsolutePath) ++
      referenceSpecies.collect { case species => Seq("-cv", s"species=$species") }.getOrElse(Seq()) ++
      referenceName.collect { case name => Seq("-cv", s"reference_name=$name") }.getOrElse(Seq()) ++
      aligner.collect { case aligner => Seq("-cv", s"aligner=$aligner") }.getOrElse(Seq())
  }

  def sampleConfig: Map[String, Any]
}

