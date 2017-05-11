package nl.lumc.sasc.biopet.test.downloadgenomes

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
  * Created by pjvan_thof on 13-5-16.
  */
trait DownloadGenomes extends Pipeline {
  def pipelineName = "downloadgenomes"

  def referenceConfigs: List[File] = Nil

  override def args =
    super.args ++
      referenceConfigs.flatMap(x => cmdArg("--referenceconfigfiles", x))
}
