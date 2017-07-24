package nl.lumc.sasc.biopet.test.basty

import nl.lumc.sasc.biopet.test.MultisampleMapping
import nl.lumc.sasc.biopet.test.aligners.Aligner
import nl.lumc.sasc.biopet.test.references.Reference

/**
  * Created by pjvan_thof on 5/26/15.
  */
trait Basty extends MultisampleMapping with Reference with Aligner {

  def pipelineName = "basty"
}
