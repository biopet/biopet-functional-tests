package nl.lumc.sasc.biopet.test.kopisu

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
  * Created by Sander Bollen on 29-12-16.
  */
trait CnMopsMethod extends Pipeline {

  override def args =
    super.args ++ cmdConfig("use_cnmops_method", "true") ++ cmdConfig("use_freec_method", "false")

}
