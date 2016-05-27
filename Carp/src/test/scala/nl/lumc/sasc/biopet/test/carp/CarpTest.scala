package nl.lumc.sasc.biopet.test.carp

import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.samples.{ Chip1, Chip2 }

/**
 * Created by pjvan_thof on 27-5-16.
 */
class CarpTest extends CarpSuccess with BwaMem with TestReference with Chip1 with Chip2 {
  def paired = true
  def shouldHaveKmerContent = true

  override def controls = super.controls ++ List("chip1" -> "chip2", "chip2" -> "chip1")
}
