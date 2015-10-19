package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepPairedClipTrimTest extends FlexiprepPaired {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("b88c68ac883086d558a76cd2fa9252fc")
  override def md5SumOutputR2 = Some("2acfae7d3ead4c6054786a1b5eef2b17")
}
