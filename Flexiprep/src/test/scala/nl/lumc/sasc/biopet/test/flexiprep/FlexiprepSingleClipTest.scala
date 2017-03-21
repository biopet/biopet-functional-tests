package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils

class FlexiprepSingleClipTest extends FlexiprepSingle with FlexiprepSingleClipSummaryValues {

  override def skipClip = Some(false)
  override def skipTrim = Some(true)

}

trait FlexiprepSingleClipSummaryValues extends FlexiprepSingle {

  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 32.23529411764706)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 33)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 30)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 22.222873900293255)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 30)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 35)

  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 17.497456765005087)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 11.90233977619532)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 51.88199389623601)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 18.71820956256358)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 22.87390029325513)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 21.26099706744868)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 26.099706744868033)
  addStatsTest(fastqcR1QcGroup, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 29.765395894428153)

  addStatsTest(fastqcR1QcGroup, "adapters" :: Nil, _ shouldBe Map(
    "Illumina Single End Adapter 1" -> "GATCGGAAGAGCTCGTATGCCGTCTTCTGCTTG",
    "Illumina PCR Primer Index 8" -> "CAAGCAGAAGACGGCATACGAGATTCAAGTGTGACTGGAGTTC"
  ))

  addStatsTest(seqstatR1QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 89086)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 18388)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 21047)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 23363)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 23777)
  addStatsTest(seqstatR1QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 2511)
  addStatsTest(seqstatR1QcGroup, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    if (inputEncodingR1 == "solexa") array(41) shouldBe 0
    else  array(41) shouldBe 15820
    array(2) shouldBe 5385
  })

  addStatsTest(seqstatR1QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 986)
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 145)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 1)
  addStatsTest(seqstatR1QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(seqstatR1QcGroup, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 986
    map("60") shouldBe 0
  })

}
