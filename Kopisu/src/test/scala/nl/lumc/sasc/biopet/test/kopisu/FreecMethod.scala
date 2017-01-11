package nl.lumc.sasc.biopet.test.kopisu

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by Sander Bollen on 29-12-16.
 */
trait FreecMethod extends Pipeline {

  override def args = super.args ++
    cmdConfig("use_freec_method", "true") ++
    cmdConfig("chrFiles", Biopet.fixtureFile("./reference/freec/contigs")) ++
    cmdConfig("chrLenFile", Biopet.fixtureFile("./reference/freec/chrLen"))

}
