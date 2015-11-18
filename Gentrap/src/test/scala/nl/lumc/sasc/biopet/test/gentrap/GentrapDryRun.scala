package nl.lumc.sasc.biopet.test.gentrap

import nl.lumc.sasc.biopet.test.aligners.Gsnap
import nl.lumc.sasc.biopet.test.references.HsapiensGRCh38
import nl.lumc.sasc.biopet.test.samples.Rna1

/**
 * Created by pjvanthof on 05/11/15.
 */
class GentrapDryRunGsnapTest extends Gentrap
  with Gsnap
  with HsapiensGRCh38
  with Rna1
  with AllExpressionMeasures {

  override def run = false

  def strandProtocol = Option("non_specific")
}
