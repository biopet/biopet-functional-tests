package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JArray

class FlexiprepPairedClipTest extends FlexiprepPaired with FlexiprepSingleClipSummaryValues {
  override def skipClip = Some(false)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")
  override def md5SumOutputR2 = Some("706f9a97239d1e2110d7b48605559d22")

  addSummaryTest(statsFastqcR2QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should haveValue(11.36105476673428),
      _ \ "1" \ "median" should haveValue(2),
      _ \ "1" \ "lower_quartile" should haveValue(2),
      _ \ "1" \ "upper_quartile" should haveValue(31),
      _ \ "1" \ "percentile_10th" should haveValue(2),
      _ \ "1" \ "percentile_90th" should haveValue(33),
      _ \ "100" \ "mean" should haveValue(5.8438133874239355),
      _ \ "100" \ "median" should haveValue(2),
      _ \ "100" \ "lower_quartile" should haveValue(2),
      _ \ "100" \ "upper_quartile" should haveValue(2),
      _ \ "100" \ "percentile_10th" should haveValue(2),
      _ \ "100" \ "percentile_90th" should haveValue(27)))

  addSummaryTest(statsFastqcR2QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should haveValue(24.556213017751478),
      _ \ "1" \ "T" should haveValue(5.325443786982249),
      _ \ "1" \ "G" should haveValue(48.22485207100592),
      _ \ "1" \ "C" should haveValue(21.893491124260358),
      _ \ "100" \ "A" should haveValue(27.55102040816326),
      _ \ "100" \ "T" should haveValue(19.825072886297377),
      _ \ "100" \ "G" should haveValue(30.612244897959183),
      _ \ "100" \ "C" should haveValue(22.011661807580175)))

  addSummaryTest(statsFastqcR2QcPath :+ "adapters",
    Seq(
      _ \ "Illumina RNA PCR Primer" should haveValue("AATGATACGGCGACCACCGAGATCTACACGTTCAGAGTTCTACAGTCCGA"),
      _ \ "Illumina Single End PCR Primer 1" should haveValue("AATGATACGGCGACCACCGAGATCTACACTCTTTCCCTACACGACGCTCTTCCGATCT")))

  addSummaryTest(statsSeqstatR2QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(98600),
      _ \ "nucleotides" \ "A" should haveValue(13736),
      _ \ "nucleotides" \ "T" should haveValue(11362),
      _ \ "nucleotides" \ "G" should haveValue(16153),
      _ \ "nucleotides" \ "C" should haveValue(13916),
      _ \ "nucleotides" \ "N" should haveValue(43433),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 2288,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 59223))

  addSummaryTest(statsSeqstatR2QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(986),
      _ \ "num_with_n" should haveValue(760),
      _ \ "len_min" should haveValue(100),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(986),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R2_qc" \ "fastqc_data" \ "path" should existAsFile))
}
