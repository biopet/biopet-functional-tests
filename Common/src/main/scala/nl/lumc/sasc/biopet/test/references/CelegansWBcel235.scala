package nl.lumc.sasc.biopet.test.references

import java.io.File

import scala.reflect.io

/**
 * Created by pjvanthof on 14/11/15.
 */
trait CelegansWBcel235 extends GlobalReference {
  def referenceSpecies = Some("C.elegans")
  def referenceName = Some("WBcel235")

}
