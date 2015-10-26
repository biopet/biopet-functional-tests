package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JArray

class FlexiprepPairedTrimTest extends FlexiprepPaired {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("1a468148f163dfc8cd46e479706efadd")
  override def md5SumOutputR2 = Some("13cc49a8e8203a634e7bbe07723f13b7")

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should haveValue(32.62888888888889),
      _ \ "1" \ "median" should haveValue(34),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(31),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(30.065104166666668),
      _ \ "100" \ "median" should haveValue(32),
      _ \ "100" \ "lower_quartile" should haveValue(29),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(24),
      _ \ "100" \ "percentile_90th" should haveValue(35)))

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should haveValue(16.666666666666664),
      _ \ "1" \ "T" should haveValue(11.11111111111111),
      _ \ "1" \ "G" should haveValue(51.33333333333333),
      _ \ "1" \ "C" should haveValue(20.88888888888889),
      _ \ "100" \ "A" should haveValue(29.427083333333332),
      _ \ "100" \ "T" should haveValue(21.875),
      _ \ "100" \ "G" should haveValue(25.520833333333332),
      _ \ "100" \ "C" should haveValue(23.177083333333336)))

  addSummaryTest(statsFastqcR1QcPath :+ "adapters",
    Seq(
      _ \ "TruSeq Adapter, Index 18" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "Illumina Multiplexing PCR Primer 2.01" should haveValue("GTGACTGGAGTTCAGACGTGTGCTCTTCCGATCT"),
      _ \ "TruSeq Adapter, Index 1" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG")))

  addSummaryTest(statsSeqstatR1QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(43153),
      _ \ "nucleotides" \ "A" should haveValue(9638),
      _ \ "nucleotides" \ "T" should haveValue(10561),
      _ \ "nucleotides" \ "G" should haveValue(11410),
      _ \ "nucleotides" \ "C" should haveValue(11544),
      _ \ "nucleotides" \ "N" should haveValue(0),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 8330,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 27))

  addSummaryTest(statsSeqstatR1QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(450),
      _ \ "num_with_n" should haveValue(0),
      _ \ "len_min" should haveValue(30),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(450),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile))

  addSummaryTest(statsFastqcR2QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should haveValue(30.475555555555555),
      _ \ "1" \ "median" should haveValue(31),
      _ \ "1" \ "lower_quartile" should haveValue(30),
      _ \ "1" \ "upper_quartile" should haveValue(32),
      _ \ "1" \ "percentile_10th" should haveValue(27),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(26.396825396825395),
      _ \ "100" \ "median" should haveValue(0),
      _ \ "100" \ "lower_quartile" should haveValue(0),
      _ \ "100" \ "upper_quartile" should haveValue(0),
      _ \ "100" \ "percentile_10th" should haveValue(0),
      _ \ "100" \ "percentile_90th" should haveValue(0)))

  addSummaryTest(statsFastqcR2QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should haveValue(27.333333333333332),
      _ \ "1" \ "T" should haveValue(13.111111111111112),
      _ \ "1" \ "G" should haveValue(36.44444444444444),
      _ \ "1" \ "C" should haveValue(23.11111111111111),
      _ \ "100" \ "A" should haveValue(31.746031746031743),
      _ \ "100" \ "T" should haveValue(15.873015873015872),
      _ \ "100" \ "G" should haveValue(23.809523809523807),
      _ \ "100" \ "C" should haveValue(28.57142857142857)))

  addSummaryTest(statsFastqcR2QcPath :+ "adapters",
    Seq(
      _ \ "Illumina Single End PCR Primer 1" should haveValue("AATGATACGGCGACCACCGAGATCTACACTCTTTCCCTACACGACGCTCTTCCGATCT")))

  addSummaryTest(statsSeqstatR2QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(29564),
      _ \ "nucleotides" \ "A" should haveValue(7455),
      _ \ "nucleotides" \ "T" should haveValue(6286),
      _ \ "nucleotides" \ "G" should haveValue(8179),
      _ \ "nucleotides" \ "C" should haveValue(7605),
      _ \ "nucleotides" \ "N" should haveValue(39),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 2100,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 65))

  addSummaryTest(statsSeqstatR2QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(450),
      _ \ "num_with_n" should haveValue(32),
      _ \ "len_min" should haveValue(20),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(450),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R2_qc" \ "fastqc_data" \ "path" should existAsFile))
}
