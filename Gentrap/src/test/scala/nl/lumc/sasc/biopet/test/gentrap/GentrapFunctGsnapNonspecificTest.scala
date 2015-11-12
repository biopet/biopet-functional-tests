package nl.lumc.sasc.biopet.test.gentrap

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline, Samples }, Pipeline._

/** Functional test for Gentrap with complete options, using gsnap and non_specific strand protocol. */
class GentrapFunctGsnapNonspecificTest extends GentrapSuccess with GentrapRefSeq {

  override def functionalTest = true

  def samples = Map("rna1" -> List("lib1"))

  override def expressionMeasures = List(
    "fragments_per_gene", "bases_per_gene", "bases_per_exon",
    "cufflinks_strict", "cufflinks_guided", "cufflinks_blind")

  override def strandProtocol = Option("non_specific")

  override def configs = super.configs :+ Samples.rna1Config

  override def referenceSpecies = Option("H.sapiens")

  override def referenceName = Option("hg19_mini")

  override def aligner = Option("gsnap")

  override def args = super.args ++
    cmdConfig("dir", Biopet.fixtureFile("gentrap").getAbsolutePath) ++
    cmdConfig("db", "hg19_mini")
}
