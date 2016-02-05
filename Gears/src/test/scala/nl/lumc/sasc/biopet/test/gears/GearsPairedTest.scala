package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.samples.{ Mgm4459735_3_050Lib1, Mgm4457768_3_050Lib1 }

/**
 * Created by pjvan_thof on 2/2/16.
 */
class GearsPairedQiimeClosedTest extends GearsSuccess with Mgm4457768_3_050Lib1 with Mgm4459735_3_050Lib1 {
  override def gearsUseKraken = Some(false)
  override def gearUseQiimeClosed = Some(true)
}

class GearsPairedKrakenTest extends GearsSuccess with Mgm4457768_3_050Lib1 with Mgm4459735_3_050Lib1 {
  override def gearsUseKraken = Some(true)
}

class GearsPairedKrakenQiimeTest extends GearsSuccess with Mgm4457768_3_050Lib1 with Mgm4459735_3_050Lib1 {
  override def gearsUseKraken = Some(true)
  override def gearUseQiimeClosed = Some(true)
}
