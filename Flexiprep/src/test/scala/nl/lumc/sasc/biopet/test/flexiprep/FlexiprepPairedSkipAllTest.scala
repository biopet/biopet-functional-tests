package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s._

class FlexiprepPairedSkipAllTest extends FlexiprepPaired {
  override def skipClip = Some(true)
  override def skipTrim = Some(true)

  addSummaryTest(statsFastqcR2QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should haveValue(11.351),
      _ \ "1" \ "median" should haveValue(2),
      _ \ "1" \ "lower_quartile" should haveValue(2),
      _ \ "1" \ "upper_quartile" should haveValue(31),
      _ \ "1" \ "percentile_10th" should haveValue(2),
      _ \ "1" \ "percentile_90th" should haveValue(33),
      _ \ "100" \ "mean" should haveValue(5.79),
      _ \ "100" \ "median" should haveValue(2),
      _ \ "100" \ "lower_quartile" should haveValue(2),
      _ \ "100" \ "upper_quartile" should haveValue(2),
      _ \ "100" \ "percentile_10th" should haveValue(2),
      _ \ "100" \ "percentile_90th" should haveValue(26)))

  addSummaryTest(statsFastqcR2QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should haveValue(24.198250728862973),
      _ \ "1" \ "T" should haveValue(5.247813411078718),
      _ \ "1" \ "G" should haveValue(48.68804664723032),
      _ \ "1" \ "C" should haveValue(21.865889212827987),
      _ \ "100" \ "A" should haveValue(27.769784172661872),
      _ \ "100" \ "T" should haveValue(19.568345323741006),
      _ \ "100" \ "G" should haveValue(30.79136690647482),
      _ \ "100" \ "C" should haveValue(21.8705035971223)))

  addSummaryTest(statsFastqcR2QcPath :+ "adapters", Seq(_.children.size shouldBe 0))

  addSummaryTest(statsSeqstatR2QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(100000),
      _ \ "nucleotides" \ "A" should haveValue(13981),
      _ \ "nucleotides" \ "T" should haveValue(11508),
      _ \ "nucleotides" \ "G" should haveValue(16442),
      _ \ "nucleotides" \ "C" should haveValue(14089),
      _ \ "nucleotides" \ "N" should haveValue(43980),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 2288,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 60383))

  addSummaryTest(statsSeqstatR2QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(1000),
      _ \ "num_with_n" should haveValue(769),
      _ \ "len_min" should haveValue(100),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(1000),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(
      _ \ "fastqc_R2_qc" \ "fastqc_data" \ "path" should existAsFile))
}
