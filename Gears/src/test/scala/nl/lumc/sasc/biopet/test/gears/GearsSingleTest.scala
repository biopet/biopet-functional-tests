package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.samples.{Mgm4459735_3_050Lib2, Mgm4457768_3_050Lib2}

/**
  * Created by pjvan_thof on 2/2/16.
  */
class GearsSingleSkipFlexiprepTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib2
    with Mgm4459735_3_050Lib2 {
  override def skipFlexiprep = Some(true)
  override def gearUseQiimeClosed = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = false
}

class GearsSingleQiimeClosedTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib2
    with Mgm4459735_3_050Lib2 {
  override def gearUseQiimeClosed = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = false
}

class GearsSingleQiimeOpenTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib2
    with Mgm4459735_3_050Lib2 {
  override def gearUseQiimeOpen = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = false
}

class GearsSingleKrakenTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib2
    with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = false
}

class GearsSingleCentrifugeTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib2
    with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(false)
  override def gearsUseCentrifuge = Some(true)
  def paired = false
}

class GearsSingleKrakenQiimeTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib2
    with Mgm4459735_3_050Lib2 {
  override def gearsUseKraken = Some(true)
  override def gearUseQiimeClosed = Some(true)
  override def gearUseQiimeOpen = Some(true)
  override def gearsUseCentrifuge = Some(true)
  def paired = false
}
