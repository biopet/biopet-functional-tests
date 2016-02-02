package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.samples.Mgm4457768_3_050Lib1

/**
 * Created by pjvan_thof on 2/2/16.
 */
class GearsQiimeClosedTest extends GearsSuccess with Mgm4457768_3_050Lib1 {
  override def gearsUseKraken = Some(false)
  override def gearUseQiimeClosed = Some(true)
}

class GearsKrakenTest extends GearsSuccess with Mgm4457768_3_050Lib1 {
  override def gearsUseKraken = Some(true)
}
