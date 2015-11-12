package nl.lumc.sasc.biopet.test.gentrap

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline, Samples }, Pipeline._

/**
 * Created by pjvanthof on 05/11/15.
 */
class GentrapDryRunGsnapTest extends Gentrap with GentrapRefSeq {

  override def run = false

  override def expressionMeasures = List(
    "fragments_per_gene", "bases_per_gene", "bases_per_exon",
    "cufflinks_strict", "cufflinks_guided", "cufflinks_blind")

  override def configs = super.configs :+ Samples.rnaMultipleConfig

  override def strandProtocol = Option("non_specific")

  override def referenceSpecies = Option("H.sapiens")

  override def referenceName = Option("hg19_mini")

  override def referenceFasta = Option(Biopet.fixtureFile("gentrap", "hg19_mini.fa"))

  override def aligner = Option("gsnap")

  override def args = super.args ++
    cmdConfig("dir", Biopet.fixtureFile("gentrap").getAbsolutePath) ++
    cmdConfig("db", "hg19_mini")
}
