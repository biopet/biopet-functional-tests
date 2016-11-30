package nl.lumc.sasc.biopet.test.references

/**
 * Created by pjvanthof on 14/11/15.
 */
trait HsapiensHg19 extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("hg19")

}
