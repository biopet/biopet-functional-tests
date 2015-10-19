package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepSingleTrimTest extends FlexiprepSingle {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5001a539ca3cc3312835466bdb37b3d8")
}
