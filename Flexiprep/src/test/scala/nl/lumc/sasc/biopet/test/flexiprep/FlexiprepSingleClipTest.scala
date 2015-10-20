package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s._

class FlexiprepSingleClipTest extends FlexiprepSingle {

  override def skipClip = Some(false)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")

  addSummaryTest(statsFastqcR1Path :+ "per_base_sequence_quality",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "mean" should haveValue(32.23529411764706),
      _ \ "1" \ "median" should haveValue(33),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(30),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(21.59223300970874),
      _ \ "100" \ "median" should haveValue(29),
      _ \ "100" \ "lower_quartile" should haveValue(2),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(2),
      _ \ "100" \ "percentile_90th" should haveValue(35)))
}
