package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.samples.{Mgm4459735_3_050Lib1, Mgm4457768_3_050Lib1}

/**
  * Created by pjvan_thof on 2/2/16.
  */
class GearsPairedSkipFlexiprepTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib1
    with Mgm4459735_3_050Lib1 {
  override def skipFlexiprep = Some(true)
  override def gearUseQiimeClosed = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = true
}

class GearsPairedQiimeClosedTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib1
    with Mgm4459735_3_050Lib1 {
  override def gearUseQiimeClosed = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = true
}

class GearsPairedQiimeOpenTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib1
    with Mgm4459735_3_050Lib1 {
  override def gearUseQiimeOpen = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = true
}

class GearsPairedKrakenTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib1
    with Mgm4459735_3_050Lib1 {
  override def gearsUseKraken = Some(true)
  override def gearsUseCentrifuge = Some(false)
  def paired = true
}

class GearsPairedCentrifugeTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib1
    with Mgm4459735_3_050Lib1 {
  override def gearsUseCentrifuge = Some(true)
  def paired = true
}

class GearsPairedKrakenQiimeTest
    extends GearsSuccess
    with Mgm4457768_3_050Lib1
    with Mgm4459735_3_050Lib1 {
  override def gearsUseKraken = Some(true)
  override def gearUseQiimeClosed = Some(true)
  override def gearUseQiimeOpen = Some(true)
  override def gearsUseCentrifuge = Some(true)
  def paired = true
}
