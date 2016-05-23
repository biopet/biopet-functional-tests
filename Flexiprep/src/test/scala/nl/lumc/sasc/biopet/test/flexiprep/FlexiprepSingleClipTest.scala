package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JsonAST.JArray

class FlexiprepSingleClipTest extends FlexiprepSingle with FlexiprepSingleClipSummaryValues {

  override def skipClip = Some(false)
  override def skipTrim = Some(true)

}

trait FlexiprepSingleClipSummaryValues extends FlexiprepSingle {

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should inInterval(32.23529411764706, 0.01),
      _ \ "1" \ "median" should haveValue(33),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(30),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should inInterval(21.538028169014083, 0.01),
      _ \ "100" \ "median" should haveValue(29),
      _ \ "100" \ "lower_quartile" should haveValue(2),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(2),
      _ \ "100" \ "percentile_90th" should haveValue(35)))

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should inInterval(17.497456765005087, 0.01),
      _ \ "1" \ "T" should inInterval(11.90233977619532, 0.01),
      _ \ "1" \ "G" should inInterval(51.88199389623601, 0.01),
      _ \ "1" \ "C" should inInterval(18.71820956256358, 0.01),
      _ \ "100" \ "A" should inInterval(24.549237170596395, 0.01),
      _ \ "100" \ "T" should inInterval(20.804438280166433, 0.01),
      _ \ "100" \ "G" should inInterval(25.211267605633804, 0.01),
      _ \ "100" \ "C" should inInterval(29.26490984743412, 0.01)))

  addSummaryTest(statsFastqcR1QcPath :+ "adapters",
    Seq(
      _ \ "Illumina PCR Primer Index 8" should haveValue("CAAGCAGAAGACGGCATACGAGATTCAAGTGTGACTGGAGTTC"),
      _ \ "Illumina Single End Adapter 1" should haveValue("GATCGGAAGAGCTCGTATGCCGTCTTCTGCTTG")))

  addSummaryTest(statsSeqstatR1QcPath :+ "bases",
    Seq(
      _ \ "num_total" should inInterval(90291, 0.01),
      _ \ "nucleotides" \ "A" should inInterval(18687, 0.01),
      _ \ "nucleotides" \ "T" should inInterval(21177, 0.01),
      _ \ "nucleotides" \ "G" should inInterval(23560, 0.01),
      _ \ "nucleotides" \ "C" should inInterval(23958, 0.01),
      _ \ "nucleotides" \ "N" should inInterval(2909, 0.01),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 15850,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 6007))

  addSummaryTest(statsSeqstatR1QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(986),
      _ \ "num_with_n" should haveValue(166),
      _ \ "len_min" should haveValue(1),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(986),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile))
}
