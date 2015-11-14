package nl.lumc.sasc.biopet.test.references

import java.io.File

import scala.reflect.io

/**
 * Created by pjvanthof on 14/11/15.
 */
trait CelegansWBcel235 extends Reference {
  override def referenceSpecies = Some("C.elegans")
  override def referenceName = Some("WBcel235")

  /** This value should be in the global config */
  def referenceFasta = None

  /** This value should be in the global config */
  def bwaMemFasta = None

  /** This value should be in the global config */
  def bowtieFasta: Option[File] = None

  /** This value should be in the global config */
  def tophatIndex: Option[String] = None

  /** This value should be in the global config */
  def gsnapDir: Option[File] = None

  /** This value should be in the global config */
  def gsnapDb: Option[String] = None

  /** This value should be in the global config */
  def starGenomeDir: Option[File] = None
}
