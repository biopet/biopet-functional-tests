package nl.lumc.sasc.biopet.test.flexiprep

class FlexiprepSingleDefaultTest extends FlexiprepSingleClipTrimTest {
  override def skipClip: Option[Boolean] = None
  override def skipTrim: Option[Boolean] = None
}
