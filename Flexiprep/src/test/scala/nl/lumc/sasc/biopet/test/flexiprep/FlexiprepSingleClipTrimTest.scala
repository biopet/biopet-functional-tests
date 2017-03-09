package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils

class FlexiprepSingleClipTrimTest extends FlexiprepSingle {
  override def skipClip: Option[Boolean] = Some(false)
  override def skipTrim: Option[Boolean] = Some(false)

  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 32.43048403707518)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 33)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 29.8752688172043)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 32)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 29)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 23)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 35)

  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 16.065911431513904)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 12.1524201853759)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 52.832131822863026)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 18.949536560247168)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 24.086021505376344)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 22.58064516129032)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 25.806451612903224)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 27.526881720430108)

  addStatsTest(fastqcR1QcGroup, "adapters" :: Nil, _ shouldBe Map(
    "Illumina PCR Primer Index 8" -> "CAAGCAGAAGACGGCATACGAGATTCAAGTGTGACTGGAGTTC",
    "Illumina Single End Adapter 1" -> "GATCGGAAGAGCTCGTATGCCGTCTTCTGCTTG"
  ))

  addStatsTest(seqstatR1QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 81844)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 17398)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 20159)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 21899)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 22384)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 4)
  addStatsTest(seqstatR1QcGroup, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 15820
    array(2) shouldBe 40
  })

  addStatsTest(seqstatR1QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 971)
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 4)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 20)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 971
    map("60") shouldBe 0
  })

}
