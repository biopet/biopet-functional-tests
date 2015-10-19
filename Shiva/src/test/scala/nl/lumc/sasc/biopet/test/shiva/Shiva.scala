package nl.lumc.sasc.biopet.test.shiva

import java.io.{ PrintWriter, File }

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by pjvan_thof on 5/26/15.
 */
trait Shiva extends Pipeline {

  def pipelineName = "shiva"

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def summaryFile = new File(outputDir, s"Shiva.summary.json")

  def args = cmdConfig("species", referenceSpecies) ++
    cmdConfig("reference_name", referenceName) ++
    cmdConfig("aligner", aligner)
}

