package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.samples.Wgs1SingleEnd
import nl.lumc.sasc.biopet.test.shiva.variantcallers.Unifiedgenotyper

/**
  * Created by pjvan_thof on 2/5/16.
  */
trait ShivaWgs1SingleEnd extends ShivaSuccess with BwaMem with TestReference with Wgs1SingleEnd {
  def paired = false
  def shouldHaveKmerContent = Some(false)
  override def dbsnpVcfFile = Some(Biopet.fixtureFile("samples", "wgs2", "wgs2.vcf.gz"))
}

class Wgs1SingleEndUnifiedGenotyperTest extends ShivaWgs1SingleEnd with Unifiedgenotyper
