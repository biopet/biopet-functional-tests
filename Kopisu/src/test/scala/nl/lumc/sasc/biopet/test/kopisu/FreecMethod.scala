package nl.lumc.sasc.biopet.test.kopisu

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by Sander Bollen on 29-12-16.
 */
trait FreecMethod extends Pipeline {

  override def args = super.args ++
    cmdConfig("use_freec_method", "true")

}
