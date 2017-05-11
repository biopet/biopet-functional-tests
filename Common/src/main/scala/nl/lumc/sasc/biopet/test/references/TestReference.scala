package nl.lumc.sasc.biopet.test.references

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
  * Created by pjvanthof on 14/11/15.
  */
trait TestReference extends Reference {
  def referenceSpecies = Some("test")
  def referenceName = Some("test")
  def referenceFasta = Some(Biopet.fixtureFile("reference/reference.fasta"))
  def bwaMemFasta = Some(Biopet.fixtureFile("reference/bwa/reference.fasta"))

  def bowtieIndex: Option[File] = Some(new File(Biopet.fixtureDir, "reference/bowtie/reference"))

  def bowtie2Index: Option[File] = Some(new File(Biopet.fixtureDir, "reference/bowtie2/reference"))

  def tophatIndex: Option[String] =
    Some(Biopet.fixtureDir + File.separator + "reference/bowtie2/reference")

  def gsnapDir: Option[File] = Some(new File(Biopet.fixtureDir, "reference/gmap"))

  def gsnapDb: Option[String] = Some("reference")

  def starGenomeDir: Option[File] = Some(Biopet.fixtureFile("reference/star"))

  def hisat2Index: Option[String] =
    Some(Biopet.fixtureDir + File.separator + "reference/hisat2/reference")
}
