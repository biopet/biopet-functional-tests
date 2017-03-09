package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils

class FlexiprepPairedClipTrimTest extends FlexiprepPairedClipTrimSummaryValues {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
}

trait FlexiprepPairedClipTrimSummaryValues extends FlexiprepPaired {

  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 32.64253393665158)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 30.315384615384616)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 32)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 29)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 24)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 35)

  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 15.610859728506787)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 11.312217194570136)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 51.80995475113123)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 21.266968325791854)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 28.40909090909091)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 20.075757575757574)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 28.030303030303028)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 23.484848484848484)

  addStatsTest(fastqcR1QcGroup, "adapters" :: Nil, _ shouldBe Map())

  addStatsTest(seqstatR1QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 38862)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 8396)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 9720)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 10384)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 10362)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 0)
  addStatsTest(seqstatR1QcGroup, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 8028
    array(2) shouldBe 17
  })

  addStatsTest(seqstatR1QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 442)
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 0)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 20)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 442
    map("60") shouldBe 0
  })

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 30.479638009049772)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 30)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 32)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 27)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 26.396825396825395)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 0)

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 27.149321266968325)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 13.122171945701359)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 36.425339366515836)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 23.30316742081448)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 31.746031746031743)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 15.873015873015872)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 23.809523809523807)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 28.57142857142857)

  addStatsTest(fastqcR2QcGroup, "adapters" :: Nil, _ shouldBe Map(
    "Illumina Single End PCR Primer 1" -> "AATGATACGGCGACCACCGAGATCTACACTCTTTCCCTACACGACGCTCTTCCGATCT"
  ))

  addStatsTest(seqstatR2QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 29166)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 7342)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 6196)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 8046)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 7545)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 37)
  addStatsTest(seqstatR2QcGroup, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 2089
    array(2) shouldBe 63
  })

  addStatsTest(seqstatR2QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 442)
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 31)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 20)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 442
    map("60") shouldBe 0
  })

}
