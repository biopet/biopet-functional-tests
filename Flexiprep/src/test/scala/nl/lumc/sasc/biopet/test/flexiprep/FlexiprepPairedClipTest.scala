package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils

class FlexiprepPairedClipTest extends FlexiprepPaired with FlexiprepSingleClipSummaryValues {
  override def skipClip = Some(false)
  override def skipTrim = Some(true)

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 11.36105476673428)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 33)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 5.8438133874239355)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 27)

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 24.556213017751478)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 5.325443786982249)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 48.22485207100592)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 21.893491124260358)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 27.55102040816326)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 19.825072886297377)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 30.612244897959183)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 22.011661807580175)

  addStatsTest(fastqcR2QcGroup, "adapters" :: Nil, _ shouldBe Map(
    "Illumina RNA PCR Primer" -> "AATGATACGGCGACCACCGAGATCTACACGTTCAGAGTTCTACAGTCCGA",
    "Illumina Single End PCR Primer 1" -> "AATGATACGGCGACCACCGAGATCTACACTCTTTCCCTACACGACGCTCTTCCGATCT"
  ))

  addStatsTest(seqstatR2QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 98600)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 13736)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 11362)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 16153)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 13916)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 43433)
  addStatsTest(seqstatR2QcGroup, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 2288
    array(2) shouldBe 59223
  })

  addStatsTest(seqstatR2QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 986)
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 760)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 986
    map("60") shouldBe 0
  })

}
