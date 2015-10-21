package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s._

class FlexiprepSingleSkipAllTest extends FlexiprepSingle {
  override def skipClip = Some(true)
  override def skipTrim = Some(true)

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "mean" should haveValue(32.244),
      _ \ "1" \ "median" should haveValue(33),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(30),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(21.984),
      _ \ "100" \ "median" should haveValue(30),
      _ \ "100" \ "lower_quartile" should haveValue(2),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(2),
      _ \ "100" \ "percentile_90th" should haveValue(35)))

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "A" should haveValue(17.251755265797392),
      _ \ "1" \ "T" should haveValue(11.735205616850552),
      _ \ "1" \ "G" should haveValue(52.35707121364093),
      _ \ "1" \ "C" should haveValue(18.655967903711137),
      _ \ "100" \ "A" should haveValue(26),
      _ \ "100" \ "T" should haveValue(21.9),
      _ \ "100" \ "G" should haveValue(24),
      _ \ "100" \ "C" should haveValue(28.1)))

  addSummaryTest(statsFastqcR1QcPath :+ "adapters",
    Seq(
      _ \ "TruSeq Adapter, Index 1" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "TruSeq Adapter, Index 18" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG")))

  addSummaryTest(statsSeqstatR1QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(100000),
      _ \ "nucleotides" \ "A" should haveValue(21644),
      _ \ "nucleotides" \ "T" should haveValue(23049),
      _ \ "nucleotides" \ "G" should haveValue(25816),
      _ \ "nucleotides" \ "C" should haveValue(26555),
      _ \ "nucleotides" \ "N" should haveValue(2936),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 16497,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 7264))

  addSummaryTest(statsSeqstatR1QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(1000),
      _ \ "num_with_n" should haveValue(175),
      _ \ "len_min" should haveValue(100),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(1000),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(
      _ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile,
      _ \ "fastqc_R1_qc" \ "fastqc_data" \ "md5" should haveValue("f2dcd9495284aac1de8c4887fbc4131f")))
}
