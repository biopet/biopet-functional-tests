package nl.lumc.sasc.biopet.test.references

import java.io.File

/**
 * Created by pjvanthof on 14/11/15.
 */
trait HsapiensGRCh37_p13_no_alt_analysis_set extends Reference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh37_p13_no_alt_analysis_set")

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

