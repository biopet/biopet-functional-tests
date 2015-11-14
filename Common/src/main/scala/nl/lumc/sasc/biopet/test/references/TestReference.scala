package nl.lumc.sasc.biopet.test.references

/**
  * Created by pjvanthof on 14/11/15.
  */
trait TestReference extends Reference {
  def referenceSpecies = Some("test")
  def referenceName = Some("test")
  def referenceFasta = Some(Biopet.fixtureFile("reference/reference.fasta"))
  def bwaMemFasta = Some(Biopet.fixtureFile("reference/bwa/reference.fasta"))
}
