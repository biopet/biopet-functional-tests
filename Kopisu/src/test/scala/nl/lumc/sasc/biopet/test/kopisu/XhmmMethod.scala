package nl.lumc.sasc.biopet.test.kopisu

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by Sander Bollen on 29-12-16.
 */
trait XhmmMethod extends Pipeline {

  override def args = super.args ++ cmdArg("use_xhmm_method", "true")
}
