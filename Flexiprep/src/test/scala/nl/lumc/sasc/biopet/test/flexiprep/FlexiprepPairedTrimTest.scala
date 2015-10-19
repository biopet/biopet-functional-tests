package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepPairedTrimTest extends FlexiprepPaired {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("1a468148f163dfc8cd46e479706efadd")
  override def md5SumOutputR2 = Some("13cc49a8e8203a634e7bbe07723f13b7")
}
