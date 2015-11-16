package nl.lumc.sasc.biopet.test.gentrap

import nl.lumc.sasc.biopet.test.references.HsapiensGRCh38
import nl.lumc.sasc.biopet.test.samples.Rna1
import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }, Pipeline._

/**
 * Created by pjvanthof on 05/11/15.
 */
class GentrapDryRunGsnapTest extends Gentrap with GentrapRefSeq with HsapiensGRCh38 with AllExpressionMeasures {

  override def run = false

  override def configs = super.configs :+ Rna1.lib1ConfigFile

  override def strandProtocol = Option("non_specific")

  override def aligner = Option("gsnap")

  override def args = super.args ++
    cmdConfig("dir", Biopet.fixtureFile("gentrap").getAbsolutePath) ++
    cmdConfig("db", "hg19_mini")
}
