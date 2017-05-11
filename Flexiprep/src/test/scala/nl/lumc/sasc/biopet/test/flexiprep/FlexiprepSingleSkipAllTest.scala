package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils

class FlexiprepSingleSkipAllTest extends FlexiprepSingle with FlexiprepSingleSkipAllSummary {
  override def skipClip = Some(true)
  override def skipTrim = Some(true)
}

trait FlexiprepSingleSkipAllSummary extends FlexiprepSingle {
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "mean" :: Nil,
               _ shouldBe 32.244)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "median" :: Nil,
               _ shouldBe 33)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil,
               _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil,
               _ shouldBe 30)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "mean" :: Nil,
               _ shouldBe 21.984)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "median" :: Nil,
               _ shouldBe 30)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil,
               _ shouldBe 2)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil,
               _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil,
               _ shouldBe 2)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil,
               _ shouldBe 35)

  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "A" :: Nil,
               _ shouldBe 17.251755265797392)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "T" :: Nil,
               _ shouldBe 11.735205616850552)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "G" :: Nil,
               _ shouldBe 52.35707121364093)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "1" :: "C" :: Nil,
               _ shouldBe 18.655967903711137)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 26)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "100" :: "T" :: Nil,
               _ shouldBe 21.9)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 24)
  addStatsTest(fastqcR1QcGroup,
               "per_base_sequence_content" :: "100" :: "C" :: Nil,
               _ shouldBe 28.1)

  addStatsTest(fastqcR1QcGroup,
               "adapters" :: "TruSeq Adapter, Index 1" :: Nil,
               _ shouldBe "GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG")
  addStatsTest(fastqcR1QcGroup,
               "adapters" :: "TruSeq Adapter, Index 18" :: Nil,
               _ shouldBe "GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG")
  addStatsTest(
    fastqcR1QcGroup,
    "adapters" :: Nil,
    _ shouldBe Map(
      "TruSeq Adapter, Index 1" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG",
      "TruSeq Adapter, Index 18" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG",
      "Illumina Universal Adapter" -> "AGATCGGAAGAG"
    )
  )

  addStatsTest(seqstatR1QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 100000)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 21644)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 23049)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 25816)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 26555)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 2936)
  addStatsTest(
    seqstatR1QcGroup,
    "bases" :: "num_qual" :: Nil,
    x => {
      x.isDefined shouldBe true
      val array = ConfigUtils.any2list(x.get).toArray
      array(41) shouldBe 16497
      array(2) shouldBe 7264
    }
  )

  addStatsTest(seqstatR1QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 1000)
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 175)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(
    seqstatR1QcGroup,
    "reads" :: "num_avg_qual_gte" :: Nil,
    x => {
      x.isDefined shouldBe true
      val map = ConfigUtils.any2map(x.get)
      map.size shouldBe 61
      map("0") shouldBe 1000
      map("60") shouldBe 0
    }
  )

}
