package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.samples.Mgm4457768_3_050Lib2

/**
 * Created by pjvan_thof on 2/2/16.
 */
class GearsSingleQiimeClosedTest extends GearsSuccess with Mgm4457768_3_050Lib2 {
  override def gearsUseKraken = Some(false)
  override def gearUseQiimeClosed = Some(true)
}

class GearsSingleKrakenTest extends GearsSuccess with Mgm4457768_3_050Lib2 {
  override def gearsUseKraken = Some(true)
}
