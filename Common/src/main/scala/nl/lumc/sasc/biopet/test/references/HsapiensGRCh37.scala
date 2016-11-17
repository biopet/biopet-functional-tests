package nl.lumc.sasc.biopet.test.references

/**
 * Created by pjvanthof on 14/11/15.
 */
trait HsapiensGRCh37 extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh37")
}
