package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JArray

class FlexiprepSingleClipTrimTest extends FlexiprepSingleClipTrimSummaryValues {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
}

trait FlexiprepSingleClipTrimSummaryValues extends FlexiprepSingle {

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should inInterval(32.43048403707518, 0.01),
      _ \ "1" \ "median" should inInterval(33, 0.01),
      _ \ "1" \ "lower_quartile" should inInterval(31, 0.01),
      _ \ "1" \ "upper_quartile" should inInterval(34, 0.01),
      _ \ "1" \ "percentile_10th" should inInterval(31, 0.01),
      _ \ "1" \ "percentile_90th" should inInterval(34, 0.01),
      _ \ "100" \ "mean" should inInterval(29.863445378151262, 0.01),
      _ \ "100" \ "median" should inInterval(32, 0.01),
      _ \ "100" \ "lower_quartile" should inInterval(29, 0.01),
      _ \ "100" \ "upper_quartile" should inInterval(34, 0.01),
      _ \ "100" \ "percentile_10th" should inInterval(23, 0.01),
      _ \ "100" \ "percentile_90th" should inInterval(35, 0.01)))

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should inInterval(16.065911431513904, 0.01),
      _ \ "1" \ "T" should inInterval(12.1524201853759, 0.01),
      _ \ "1" \ "G" should inInterval(52.832131822863026, 0.01),
      _ \ "1" \ "C" should inInterval(18.949536560247168, 0.01),
      _ \ "100" \ "A" should inInterval(25, 0.01),
      _ \ "100" \ "T" should inInterval(22.058823529411764, 0.01),
      _ \ "100" \ "G" should inInterval(25, 0.01),
      _ \ "100" \ "C" should inInterval(27.100840336134453, 0.01)))

  addSummaryTest(statsFastqcR1QcPath :+ "adapters",
    Seq(
      _ \ "TruSeq Adapter, Index 18" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "Illumina Multiplexing PCR Primer 2.01" should haveValue("GTGACTGGAGTTCAGACGTGTGCTCTTCCGATCT"),
      _ \ "Illumina Single End Adapter 1" should haveValue("GATCGGAAGAGCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "Illumina PCR Primer Index 8" should haveValue("CAAGCAGAAGACGGCATACGAGATTCAAGTGTGACTGGAGTTC"),
      _ \ "TruSeq Adapter, Index 1" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG")))

  addSummaryTest(statsSeqstatR1QcPath :+ "bases",
    Seq(
      _ \ "num_total" should inInterval(82377, 0.01),
      _ \ "nucleotides" \ "A" should inInterval(17609, 0.01),
      _ \ "nucleotides" \ "T" should inInterval(20235, 0.01),
      _ \ "nucleotides" \ "G" should inInterval(22036, 0.01),
      _ \ "nucleotides" \ "C" should inInterval(22493, 0.01),
      _ \ "nucleotides" \ "N" should inInterval(4, 0.01),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 15850,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 43))

  addSummaryTest(statsSeqstatR1QcPath :+ "reads",
    Seq(
      _ \ "num_total" should inInterval(971, 0.01),
      _ \ "num_with_n" should inInterval(4, 0.01),
      _ \ "len_min" should inInterval(20, 0.01),
      _ \ "len_max" should inInterval(100, 0.01),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should inInterval(971, 0.01),
      _ \ "num_avg_qual_gte" \ "60" should inInterval(0, 0.01)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile))
}
