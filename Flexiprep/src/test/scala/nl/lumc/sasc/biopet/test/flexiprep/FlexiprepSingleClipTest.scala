package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s._

class FlexiprepSingleClipTest extends FlexiprepSingle {

  override def skipClip = Some(false)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")

  addSummaryTest(
    Seq("samples", sampleId, "libraries", libId, "flexiprep", "stats", "fastqc_R1", "per_base_sequence_quality"),
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "mean" shouldBe JDouble(32.244),
      _ \ "1" \ "median" shouldBe JInt(33),
      _ \ "1" \ "lower_quartile" shouldBe JInt(31),
      _ \ "1" \ "upper_quartile" shouldBe JInt(34),
      _ \ "1" \ "percentile_10th" shouldBe JInt(30),
      _ \ "1" \ "percentile_90th" shouldBe JInt(34),
      _ \ "100" \ "mean" shouldBe JDouble(21.984),
      _ \ "100" \ "median" shouldBe JInt(30),
      _ \ "100" \ "lower_quartile" shouldBe JInt(2),
      _ \ "100" \ "upper_quartile" shouldBe JInt(34),
      _ \ "100" \ "percentile_10th" shouldBe JInt(2),
      _ \ "100" \ "percentile_90th" shouldBe JInt(35)))

  addSummaryTest(
    Seq("samples", sampleId, "libraries", libId, "flexiprep", "stats", "fastqc_R1_qc", "per_base_sequence_quality"),
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "mean" shouldBe JDouble(32.23529411764706),
      _ \ "1" \ "median" shouldBe JInt(33),
      _ \ "1" \ "lower_quartile" shouldBe JInt(31),
      _ \ "1" \ "upper_quartile" shouldBe JInt(34),
      _ \ "1" \ "percentile_10th" shouldBe JInt(30),
      _ \ "1" \ "percentile_90th" shouldBe JInt(34),
      _ \ "100" \ "mean" shouldBe JDouble(21.59223300970874),
      _ \ "100" \ "median" shouldBe JInt(29),
      _ \ "100" \ "lower_quartile" shouldBe JInt(2),
      _ \ "100" \ "upper_quartile" shouldBe JInt(34),
      _ \ "100" \ "percentile_10th" shouldBe JInt(2),
      _ \ "100" \ "percentile_90th" shouldBe JInt(35)))
}
