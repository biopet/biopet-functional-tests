package nl.lumc.sasc.biopet.test.references

import java.io.File

/**
 * Created by pjvanthof on 14/11/15.
 */
trait RnorvegicusRnor_6 extends Reference {
  def referenceSpecies = Some("R.norvegicus")
  def referenceName = Some("Rnor_6.0")

  override def functionalTest = true

  /** This value should be in the global config */
  def referenceFasta = None

  /** This value should be in the global config */
  def bwaMemFasta = None

  /** This value should be in the global config */
  def bowtieIndex: Option[File] = None

  /** This value should be in the global config */
  def bowtie2Index: Option[File] = None

  /** This value should be in the global config */
  def tophatIndex: Option[String] = None

  /** This value should be in the global config */
  def gsnapDir: Option[File] = None

  /** This value should be in the global config */
  def gsnapDb: Option[String] = None

  /** This value should be in the global config */
  def starGenomeDir: Option[File] = None

  /** This value should be in the global config */
  def hisat2Index: Option[File] = None
}