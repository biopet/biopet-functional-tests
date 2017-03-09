package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils

class FlexiprepPairedClipTest extends FlexiprepPaired with FlexiprepSingleClipSummaryValues {
  override def skipClip = Some(false)
  override def skipTrim = Some(true)

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 11.322482197355036)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 33)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 5.453558504221954)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 24)

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 23.88059701492537)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 5.325443786982249)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 48.656716417910445)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 22.08955223880597)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 25.14177693761815)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 20.226843100189036)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 30.056710775047257)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 24.574669187145556)

  addStatsTest(fastqcR2QcGroup, "adapters" :: Nil, _ shouldBe Map(
    "Illumina RNA PCR Primer" -> "AATGATACGGCGACCACCGAGATCTACACGTTCAGAGTTCTACAGTCCGA",
    "Illumina Single End PCR Primer 1" -> "AATGATACGGCGACCACCGAGATCTACACTCTTTCCCTACACGACGCTCTTCCGATCT"
  ))

  addStatsTest(seqstatR2QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 93721)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 12333)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 10523)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 14299)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 13266)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 43300)
  addStatsTest(seqstatR2QcGroup, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 2243
    array(2) shouldBe 59223
  })

  addStatsTest(seqstatR2QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 983)
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 760)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 19)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 983
    map("60") shouldBe 0
  })

}
