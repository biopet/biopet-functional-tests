package nl.lumc.sasc.biopet.test.downloadgenomes

import nl.lumc.sasc.biopet.test.{Biopet, Pipeline}
import nl.lumc.sasc.biopet.test.Pipeline._

/**
  * Created by pjvan_thof on 13-5-16.
  */
trait ValidateAnnotations extends Pipeline {
  def pipelineName = "validateannotations"

  override def args =
    super.args ++ cmdArg("--speciesdir", Biopet.speciesDir)
}
