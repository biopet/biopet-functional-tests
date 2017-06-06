package nl.lumc.sasc.biopet.test.flexiprep

/**
  * Created by Sander Bollen on 1-6-17.
  */
class FlexiprepPairedDownSampleTest extends FlexiprepPaired {
  override def downSampleFraction = Some(0.5)

  addStatsTest(seqstatR1QcGroup,
               "bases" :: "num_total" :: Nil,
               _.asInstanceOf[Int] should equal(18736 +- 1873))
  addStatsTest(seqstatR1QcGroup,
               "reads" :: "num_total" :: Nil,
               _.asInstanceOf[Int] should equal(209 +- 20))
  addStatsTest(seqstatR2QcGroup,
               "bases" :: "num_total" :: Nil,
               _.asInstanceOf[Int] should equal(13341 +- 1334))
  addStatsTest(seqstatR2QcGroup,
               "reads" :: "num_total" :: Nil,
               _.asInstanceOf[Int] should equal(209 +- 20))

}
