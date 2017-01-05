package nl.lumc.sasc.biopet.test.kopisu

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by Sander Bollen on 29-12-16.
 */
trait XhmmMethod extends Pipeline {

  override def args = super.args ++ cmdConfig("use_xhmm_method", "true") ++
    cmdConfig("use_freec_method", "false") ++
    cmdConfig("amplicon_bed", Biopet.fixtureFile("./kopisu/targets.bed")) ++
    cmdConfig("discover_params", Biopet.fixtureFile("./kopisu/discover.params"))
}
