package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 5/26/15.
 */
abstract class AbstractMapping extends Pipeline {

  def pipelineName = "mapping"

  def sampleId = "sampleName"

  def libId = "libName"

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def args = Seq("-sample", sampleId, "-library", libId, "-cv", s"output_dir=$outputDir-run") ++
    (referenceSpecies match {
      case Some(species) => Seq("-cv", s"reference_species=$species")
      case _             => Seq()
    }) ++ (referenceName match {
      case Some(name) => Seq("-cv", s"reference_name=$name")
      case _          => Seq()
    }) ++ (aligner match {
      case Some(aln) => Seq("-cv", s"aligner=$aln")
      case _         => Seq()
    })
}
