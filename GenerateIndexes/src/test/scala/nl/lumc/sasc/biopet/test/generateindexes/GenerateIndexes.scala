package nl.lumc.sasc.biopet.test.generateindexes

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by pjvan_thof on 13-5-16.
 */
trait GenerateIndexes extends Pipeline {
  def pipelineName = "generateindexes"

  def referenceConfigs: List[File] = Nil

  override def args = super.args ++
    cmdArg("--referenceconfigfiles", referenceConfigs)
}
