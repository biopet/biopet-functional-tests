package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepPairedClipTest extends FlexiprepPaired {
  override def skipClip = Some(false)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")
  override def md5SumOutputR2 = Some("706f9a97239d1e2110d7b48605559d22")
}
