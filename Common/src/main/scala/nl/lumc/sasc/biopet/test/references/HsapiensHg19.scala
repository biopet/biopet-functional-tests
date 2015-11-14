package nl.lumc.sasc.biopet.test.references

/**
  * Created by pjvanthof on 14/11/15.
  */
trait HsapiensHg19 extends Reference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("hg19")

  /** This value should be in the global config */
  def referenceFasta = None

  /** This value should be in the global config */
  def bwaMemFasta = None
}
