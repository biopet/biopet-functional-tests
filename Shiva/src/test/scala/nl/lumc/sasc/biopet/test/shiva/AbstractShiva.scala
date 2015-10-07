package nl.lumc.sasc.biopet.test.shiva

import java.io.{ PrintWriter, File }

import nl.lumc.sasc.biopet.test.Pipeline

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
    Seq("-cv", s"output_dir=$outputDir") ++
      configs.map(x => Seq("-config", x.getAbsolutePath)).toSeq.flatten ++
      referenceSpecies.collect { case species => Seq("-cv", s"species=$species") }.getOrElse(Seq()) ++
      referenceName.collect { case name => Seq("-cv", s"reference_name=$name") }.getOrElse(Seq()) ++
      aligner.collect { case aligner => Seq("-cv", s"aligner=$aligner") }.getOrElse(Seq())
  }

  def configs: List[File]
}

