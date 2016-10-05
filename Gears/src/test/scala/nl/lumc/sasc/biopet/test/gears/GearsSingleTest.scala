package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.samples.{ Mgm4459735_3_050Lib2, Mgm4457768_3_050Lib2 }

/**
 * Created by pjvan_thof on 2/2/16.
 */
class GearsSingleQiimeClosedTest extends GearsSuccess with Mgm4457768_3_050Lib2 with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(false)
  override def gearUseQiimeClosed = Some(true)
}

class GearsSingleQiimeOpenTest extends GearsSuccess with Mgm4457768_3_050Lib2 with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(false)
  override def gearUseQiimeOpen = Some(true)
}

class GearsSingleKrakenTest extends GearsSuccess with Mgm4457768_3_050Lib2 with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(true)
}

class GearsSingleCentrifugeTest extends GearsSuccess with Mgm4457768_3_050Lib2 with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(false)
  override def gearsUseCentrifuge = Some(true)
}

class GearsSingleKrakenQiimeTest extends GearsSuccess with Mgm4457768_3_050Lib2 with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(true)
  override def gearUseQiimeClosed = Some(true)
  override def gearUseQiimeOpen = Some(true)
}
