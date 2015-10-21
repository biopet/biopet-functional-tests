package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JArray

class FlexiprepPairedClipTrimTest extends FlexiprepPaired with FlexiprepPairedClipTrimSummaryValues {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("b88c68ac883086d558a76cd2fa9252fc")
  override def md5SumOutputR2 = Some("2acfae7d3ead4c6054786a1b5eef2b17")
}

trait FlexiprepPairedClipTrimSummaryValues { this: FlexiprepPaired =>

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "mean" should haveValue(32.64253393665158),
      _ \ "1" \ "median" should haveValue(34),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(31),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(30.314393939393938),
      _ \ "100" \ "median" should haveValue(32),
      _ \ "100" \ "lower_quartile" should haveValue(29),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(24),
      _ \ "100" \ "percentile_90th" should haveValue(35)))

  addSummaryTest(statsFastqcR1QcPath :+ "per_base_sequence_content",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "A" should haveValue(15.610859728506787),
      _ \ "1" \ "T" should haveValue(11.312217194570136),
      _ \ "1" \ "G" should haveValue(51.80995475113123),
      _ \ "1" \ "C" should haveValue(21.266968325791854),
      _ \ "100" \ "A" should haveValue(28.40909090909091),
      _ \ "100" \ "T" should haveValue(20.075757575757574),
      _ \ "100" \ "G" should haveValue(28.030303030303028),
      _ \ "100" \ "C" should haveValue(23.484848484848484)))

  addSummaryTest(statsFastqcR1QcPath :+ "adapters", Seq(_.children.size shouldBe 0))

  addSummaryTest(statsSeqstatR1QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(38879),
      _ \ "nucleotides" \ "A" should haveValue(8405),
      _ \ "nucleotides" \ "T" should haveValue(9720),
      _ \ "nucleotides" \ "G" should haveValue(10387),
      _ \ "nucleotides" \ "C" should haveValue(10367),
      _ \ "nucleotides" \ "N" should haveValue(0),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 8028,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 17))

  addSummaryTest(statsSeqstatR1QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(442),
      _ \ "num_with_n" should haveValue(0),
      _ \ "len_min" should haveValue(20),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(442),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile))

  addSummaryTest(statsFastqcR2QcPath :+ "per_base_sequence_quality",
    Seq(
      _.children.size shouldBe 55,
      _ \ "1" \ "mean" should haveValue(30.479638009049772),
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
      _.children.size shouldBe 55,
      _ \ "1" \ "A" should haveValue(27.149321266968325),
      _ \ "1" \ "T" should haveValue(13.122171945701359),
      _ \ "1" \ "G" should haveValue(36.425339366515836),
      _ \ "1" \ "C" should haveValue(23.30316742081448),
      _ \ "100" \ "A" should haveValue(31.746031746031743),
      _ \ "100" \ "T" should haveValue(15.873015873015872),
      _ \ "100" \ "G" should haveValue(23.809523809523807),
      _ \ "100" \ "C" should haveValue(28.57142857142857)))

  addSummaryTest(statsFastqcR2QcPath :+ "adapters",
    Seq(
      _ \ "Illumina Single End PCR Primer 1" should haveValue("AATGATACGGCGACCACCGAGATCTACACTCTTTCCCTACACGACGCTCTTCCGATCT")))

  addSummaryTest(statsSeqstatR2QcPath :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(29166),
      _ \ "nucleotides" \ "A" should haveValue(7342),
      _ \ "nucleotides" \ "T" should haveValue(6196),
      _ \ "nucleotides" \ "G" should haveValue(8046),
      _ \ "nucleotides" \ "C" should haveValue(7545),
      _ \ "nucleotides" \ "N" should haveValue(37),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 2089,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 63))

  addSummaryTest(statsSeqstatR2QcPath :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(442),
      _ \ "num_with_n" should haveValue(31),
      _ \ "len_min" should haveValue(20),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue("sanger"),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(442),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(_ \ "fastqc_R1_qc" \ "fastqc_data" \ "path" should existAsFile))
}
