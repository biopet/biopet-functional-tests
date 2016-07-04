package nl.lumc.sasc.biopet.test.carp

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMapping
import nl.lumc.sasc.biopet.test.aligners.Aligner
import nl.lumc.sasc.biopet.test.references.Reference
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 5/26/15.
 */
trait Carp extends MultisampleMapping with Reference with Aligner {

  def pipelineName = "carp"

  def summaryFile = new File(outputDir, s"Carp.summary.json")

  def controls: List[(String, String)] = Nil

  override def configs = super.configs ++ controls.map {
    case (sample, control) =>
      createTempConfig(Map("samples" -> Map(sample -> Map("control" -> control))))
  }

  override def args = super.args
}
