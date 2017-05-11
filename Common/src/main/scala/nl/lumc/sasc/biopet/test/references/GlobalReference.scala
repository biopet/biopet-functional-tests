package nl.lumc.sasc.biopet.test.references

import java.io.File

/**
  * Created by pjvan_thof on 27-10-16.
  */
trait GlobalReference extends Reference {
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
  def hisat2Index: Option[String] = None
}

trait CelegansWBcel235 extends GlobalReference {
  def referenceSpecies = Some("C.elegans")
  def referenceName = Some("WBcel235")
}

trait ClfamiliarisCanFam3_1 extends GlobalReference {
  def referenceSpecies = Some("C.l.familiaris")
  def referenceName = Some("CanFam3.1")
}

trait HsapiensGRCh37 extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh37")
}

trait HsapiensGRCh37_p13_no_alt_analysis_set extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh37.p13_no_alt_analysis_set")
}

trait HsapiensGRCh38 extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh38")
}

trait HsapiensGRCh38_no_alt_analysis_set extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh38_no_alt_analysis_set")
}

trait HsapiensHg19 extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("hg19")
}

trait MmusculusGRCm38 extends GlobalReference {
  def referenceSpecies = Some("M.musculus")
  def referenceName = Some("GRCm38")
}

trait RnorvegicusRnor_6 extends GlobalReference {
  def referenceSpecies = Some("R.norvegicus")
  def referenceName = Some("Rnor_6.0")
}

trait DrerioGRCz10 extends GlobalReference {
  def referenceSpecies = Some("D.rerio")
  def referenceName = Some("GRCz10")
}

trait DmelanogasterBDGP6 extends GlobalReference {
  def referenceSpecies = Some("D.melanogaster")
  def referenceName = Some("BDGP6")
}
