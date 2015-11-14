package nl.lumc.sasc.biopet.test.references

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by pjvanthof on 14/11/15.
 */
trait Reference extends Pipeline {
  def referenceSpecies: Option[String]

  def referenceName: Option[String]

  def referenceFasta: Option[File]

  abstract override def args = super.args ++
    cmdConfig("species", referenceSpecies) ++
    cmdConfig("reference_name", referenceName) ++
    cmdConfig("reference_fasta", referenceFasta)
}







