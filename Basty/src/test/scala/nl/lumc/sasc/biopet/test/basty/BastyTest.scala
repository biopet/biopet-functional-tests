package nl.lumc.sasc.biopet.test.basty

import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.samples.{Wgs1, Wgs2}

/**
  * Created by pjvan_thof on 27-5-16.
  */
class BastyTest extends BastySuccess with BwaMem with TestReference with Wgs1 with Wgs2 {
  def paired = true
  def shouldHaveKmerContent = None
}
