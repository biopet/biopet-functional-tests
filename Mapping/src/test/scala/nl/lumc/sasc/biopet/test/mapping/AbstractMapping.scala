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

  def r1: Option[File] = None

  def r2: Option[File] = None

  def paired = false

  def args = Seq("-sample", sampleId, "-library", libId, "-cv", s"output_dir=$outputDir") ++
    referenceSpecies.collect { case species => Seq("-cv", s"species=$species") }.getOrElse(Seq()) ++
    referenceName.collect { case name => Seq("-cv", s"reference_name=$name") }.getOrElse(Seq()) ++
    aligner.collect { case aligner => Seq("-cv", s"aligner=$aligner") }.getOrElse(Seq()) ++
    r1.collect { case r1 => Seq("-R1", r1.getAbsolutePath) }.getOrElse(Seq()) ++
    r2.collect { case r2 if paired => Seq("-R2", r2.getAbsolutePath) }.getOrElse(Seq())
}
