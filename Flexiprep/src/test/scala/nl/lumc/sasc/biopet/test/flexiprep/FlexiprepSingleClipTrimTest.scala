package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepSingleClipTrimTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")
}
