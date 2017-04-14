package nl.lumc.sasc.biopet.test.mapping

import nl.lumc.sasc.biopet.test.MultisampleMappingSuccess
import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.samples.{ Wgs1, Wgs2 }

/**
 * Created by pjvanthof on 14/04/2017.
 */
trait TestMultisampleMapping extends MultisampleMappingSuccess
  with TestReference
  with BwaMem
  with Wgs1
  with Wgs2 {
  def paired: Boolean = true

  def shouldHaveKmerContent: Option[Boolean] = None

  /** Name of pipeline, this is used for the commandline and for the summary */
  def pipelineName: String = "multisamplemapping"
}

class DefaultTest extends TestMultisampleMapping
class PreProcessSambambaMarkdupTest extends TestMultisampleMapping {
  override def mergeStrategy = Some("preprocesssambambamarkdup")
}
