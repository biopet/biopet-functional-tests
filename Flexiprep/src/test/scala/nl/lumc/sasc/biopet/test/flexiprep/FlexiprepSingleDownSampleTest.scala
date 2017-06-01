package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s._

/**
  * Created by Sander Bollen on 1-6-17.
  */
class FlexiprepSingleDownSampleTest extends FlexiprepSingle {
  override def downSampleFraction = Some(0.5)

  addStatsTest(seqstatR1QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 40922)
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 458)

}