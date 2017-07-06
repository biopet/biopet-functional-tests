package nl.lumc.sasc.biopet.test.flexiprep

/**
  * Created by Sander Bollen on 1-6-17.
  */
class FlexiprepSingleDownSampleTest extends FlexiprepSingle {
  override def downSampleFraction = Some(0.5)

  override def skipClip: Option[Boolean] = Option(true)

  override def skipTrim: Option[Boolean] = Option(true)

  addStatsTest(seqstatR1QcGroup,
               "reads" :: "num_total" :: Nil,
               _.asInstanceOf[Long].toInt shouldBe 500)

}
