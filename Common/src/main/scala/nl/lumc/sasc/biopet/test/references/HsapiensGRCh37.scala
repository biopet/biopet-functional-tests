package nl.lumc.sasc.biopet.test.references

/**
  * Created by pjvanthof on 14/11/15.
  */
trait HsapiensGRCh37 extends Reference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh37")

  /** This value should be in the global config */
  def referenceFasta = None

  /** This value should be in the global config */
  def bwaMemFasta = None
}
