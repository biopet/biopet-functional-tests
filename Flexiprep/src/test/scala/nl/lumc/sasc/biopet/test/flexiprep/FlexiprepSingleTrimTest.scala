package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JArray

class FlexiprepSingleTrimTest extends FlexiprepSingle {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5001a539ca3cc3312835466bdb37b3d8")

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "mean" should haveValue(32.412),
      _ \ "1" \ "median" should haveValue(33),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(30),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(29.88757396449704),
      _ \ "100" \ "median" should haveValue(32),
      _ \ "100" \ "lower_quartile" should haveValue(29),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(23),
      _ \ "100" \ "percentile_90th" should haveValue(35)))

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "A" should haveValue(17),
      _ \ "1" \ "T" should haveValue(11.799999999999999),
      _ \ "1" \ "G" should haveValue(52.5),
      _ \ "1" \ "C" should haveValue(18.7),
      _ \ "100" \ "A" should haveValue(27.662721893491128),
      _ \ "100" \ "T" should haveValue(22.928994082840237),
      _ \ "100" \ "G" should haveValue(23.224852071005916),
      _ \ "100" \ "C" should haveValue(26.183431952662723)))

  addSummaryTest(statsFastqcR1QcPath :+ "adapters",
    Seq(
      _ \ "TruSeq Adapter, Index 18" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "TruSeq Adapter, Index 1" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG")))

  addSummaryTest(statsSeqstatR1QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(90659),
      _ \ "nucleotides" \ "A" should haveValue(19953),
      _ \ "nucleotides" \ "T" should haveValue(21898),
      _ \ "nucleotides" \ "G" should haveValue(24006),
      _ \ "nucleotides" \ "C" should haveValue(24795),
      _ \ "nucleotides" \ "N" should haveValue(7),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 16947,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 62))

  addSummaryTest(statsSeqstatR1QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(1000),
      _ \ "num_with_n" should haveValue(7),
      _ \ "len_min" should haveValue(21),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(1000),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile))
}
