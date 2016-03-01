package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JArray

class FlexiprepSingleClipTrimTest extends FlexiprepSingleClipTrimSummaryValues {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")
}

trait FlexiprepSingleClipTrimSummaryValues extends FlexiprepSingle {

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should haveValue(32.43048403707518),
      _ \ "1" \ "median" should haveValue(33),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(31),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(29.863445378151262),
      _ \ "100" \ "median" should haveValue(32),
      _ \ "100" \ "lower_quartile" should haveValue(29),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(23),
      _ \ "100" \ "percentile_90th" should haveValue(35)))

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should haveValue(16.065911431513904),
      _ \ "1" \ "T" should haveValue(12.1524201853759),
      _ \ "1" \ "G" should haveValue(52.832131822863026),
      _ \ "1" \ "C" should haveValue(18.949536560247168),
      _ \ "100" \ "A" should haveValue(25.630252100840334),
      _ \ "100" \ "T" should haveValue(22.058823529411764),
      _ \ "100" \ "G" should haveValue(25.210084033613445),
      _ \ "100" \ "C" should haveValue(27.100840336134453)))

  addSummaryTest(statsFastqcR1QcPath :+ "adapters",
    Seq(
      _ \ "TruSeq Adapter, Index 18" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "Illumina Multiplexing PCR Primer 2.01" should haveValue("GTGACTGGAGTTCAGACGTGTGCTCTTCCGATCT"),
      _ \ "Illumina Single End Adapter 1" should haveValue("GATCGGAAGAGCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "Illumina PCR Primer Index 8" should haveValue("CAAGCAGAAGACGGCATACGAGATTCAAGTGTGACTGGAGTTC"),
      _ \ "TruSeq Adapter, Index 1" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG")))

  addSummaryTest(statsSeqstatR1QcPath :+ "bases",
    Seq(
      _ \ "num_total" shouldBe inInterval(82407, 0.1),
      _ \ "nucleotides" \ "A" shouldBe inInterval(17626, 0.1),
      _ \ "nucleotides" \ "T" shouldBe inInterval(20235, 0.1),
      _ \ "nucleotides" \ "G" shouldBe inInterval(22040, 0.1),
      _ \ "nucleotides" \ "C" shouldBe inInterval(22502, 0.1),
      _ \ "nucleotides" \ "N" shouldBe inInterval(4, 0.1),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe inInterval(15850, 0.1),
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe inInterval(43, 0.1)))

  addSummaryTest(statsSeqstatR1QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(971),
      _ \ "num_with_n" should haveValue(4),
      _ \ "len_min" should haveValue(20),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(971),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile))
}
