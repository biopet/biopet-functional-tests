package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils

class FlexiprepPairedTrimTest extends FlexiprepPaired {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)

  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "mean" :: Nil,
               _ shouldBe 32.62888888888889)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "median" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil,
               _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil,
               _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "mean" :: Nil,
               _ shouldBe 30.065104166666668)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "median" :: Nil,
               _ shouldBe 32)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil,
               _ shouldBe 29)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil,
               _ shouldBe 24)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil,
               _ shouldBe 35)

  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "A" :: Nil,
               _ shouldBe 16.666666666666664)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "T" :: Nil,
               _ shouldBe 11.11111111111111)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "G" :: Nil,
               _ shouldBe 51.33333333333333)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "C" :: Nil,
               _ shouldBe 20.88888888888889)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "100" :: "A" :: Nil,
               _ shouldBe 29.427083333333332)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "100" :: "T" :: Nil,
               _ shouldBe 21.875)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "100" :: "G" :: Nil,
               _ shouldBe 25.520833333333332)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "100" :: "C" :: Nil,
               _ shouldBe 23.177083333333336)

  addStatsTest(
    fastqcR1QcGroup,
    "adapters" :: Nil,
    _ shouldBe Map(
      "TruSeq Adapter, Index 18" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG",
      "TruSeq Adapter, Index 1" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG",
      "Illumina Universal Adapter" -> "AGATCGGAAGAG",
      "Illumina Multiplexing PCR Primer 2.01" -> "GTGACTGGAGTTCAGACGTGTGCTCTTCCGATCT"
    )
  )

  addStatsTest(seqstatR1QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 43153)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 9638)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 10561)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 11410)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 11544)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 0)
  addStatsTest(
    seqstatR1QcGroup,
    "bases" :: "num_qual" :: Nil,
    x => {
      x.isDefined shouldBe true
      val array = ConfigUtils.any2list(x.get).toArray
      array(41) shouldBe 8330
      array(2) shouldBe 27
    }
  )

  addStatsTest(seqstatR1QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 450)
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 0)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 30)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(
    seqstatR1QcGroup,
    "reads" :: "num_avg_qual_gte" :: Nil,
    x => {
      x.isDefined shouldBe true
      val map = ConfigUtils.any2map(x.get)
      map.size shouldBe 61
      map("0") shouldBe 450
      map("60") shouldBe 0
    }
  )

  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "1" :: "mean" :: Nil,
               _ shouldBe 30.475555555555555)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "1" :: "median" :: Nil,
               _ shouldBe 31)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil,
               _ shouldBe 30)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil,
               _ shouldBe 32)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil,
               _ shouldBe 27)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "100" :: "mean" :: Nil,
               _ shouldBe 26.396825396825395)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "100" :: "median" :: Nil,
               _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil,
               _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil,
               _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil,
               _ shouldBe 0)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil,
               _ shouldBe 0)

  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "1" :: "A" :: Nil,
               _ shouldBe 27.333333333333332)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "1" :: "T" :: Nil,
               _ shouldBe 13.111111111111112)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "1" :: "G" :: Nil,
               _ shouldBe 36.44444444444444)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "1" :: "C" :: Nil,
               _ shouldBe 23.11111111111111)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "100" :: "A" :: Nil,
               _ shouldBe 31.746031746031743)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "100" :: "T" :: Nil,
               _ shouldBe 15.873015873015872)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "100" :: "G" :: Nil,
               _ shouldBe 23.809523809523807)
  addStatsTest(fastqcR2QcGroup,
               "per_base_sequence_content" :: "100" :: "C" :: Nil,
               _ shouldBe 28.57142857142857)

  addStatsTest(
    fastqcR2QcGroup,
    "adapters" :: Nil,
    _ shouldBe Map(
      "Illumina Universal Adapter" -> "AGATCGGAAGAG",
      "Illumina Single End PCR Primer 1" -> "AATGATACGGCGACCACCGAGATCTACACTCTTTCCCTACACGACGCTCTTCCGATCT"
    )
  )

  addStatsTest(seqstatR2QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 29564)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 7455)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 6286)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 8179)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 7605)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 39)
  addStatsTest(
    seqstatR2QcGroup,
    "bases" :: "num_qual" :: Nil,
    x => {
      x.isDefined shouldBe true
      val array = ConfigUtils.any2list(x.get).toArray
      array(41) shouldBe 2100
      array(2) shouldBe 65
    }
  )

  addStatsTest(seqstatR2QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 450)
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 32)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 20)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(
    seqstatR2QcGroup,
    "reads" :: "num_avg_qual_gte" :: Nil,
    x => {
      x.isDefined shouldBe true
      val map = ConfigUtils.any2map(x.get)
      map.size shouldBe 61
      map("0") shouldBe 450
      map("60") shouldBe 0
    }
  )

}
