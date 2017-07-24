package nl.lumc.sasc.biopet.test.carp

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMapping
import nl.lumc.sasc.biopet.test.aligners.Aligner
import nl.lumc.sasc.biopet.test.references.Reference
import nl.lumc.sasc.biopet.test.utils._

/**
  * Created by pjvan_thof on 5/26/15.
  */
trait Basty extends MultisampleMapping with Reference with Aligner {

  def pipelineName = "basty"
}
