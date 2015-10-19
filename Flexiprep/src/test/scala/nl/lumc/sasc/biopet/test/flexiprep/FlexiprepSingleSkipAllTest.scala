package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepSingleSkipAllTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
}
