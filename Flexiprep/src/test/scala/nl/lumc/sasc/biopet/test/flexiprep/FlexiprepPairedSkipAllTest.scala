package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.utils.ConfigUtils
import org.json4s._

class FlexiprepPairedSkipAllTest extends FlexiprepPaired with FlexiprepSingleSkipAllSummary {
  override def skipClip = Some(true)
  override def skipTrim = Some(true)

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 11.351)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 33)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 5.79)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 26)

  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 24.198250728862973)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 5.247813411078718)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 48.68804664723032)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 21.865889212827987)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 27.769784172661872)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 19.568345323741006)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 30.79136690647482)
  addStatsTest(fastqcR2QcGroup, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 21.8705035971223)

  addStatsTest(fastqcR2QcGroup, "adapters" :: Nil, _ shouldBe Map("Illumina Universal Adapter" -> "AGATCGGAAGAG"))

  addStatsTest(seqstatR2QcGroup, "bases" :: "num_total" :: Nil, _ shouldBe 100000)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 13981)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 11508)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 16442)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 14089)
  addStatsTest(seqstatR2QcGroup, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 43980)
  addStatsTest(seqstatR2QcGroup, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 2288
    array(2) shouldBe 60383
  })

  addStatsTest(seqstatR2QcGroup, "reads" :: "num_total" :: Nil, _ shouldBe 1000)
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_with_n" :: Nil, _ shouldBe 769)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_min" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2QcGroup, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2QcGroup, "reads" :: "qual_encoding" :: Nil, _ shouldBe "sanger")
  addStatsTest(seqstatR2QcGroup, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 1000
    map("60") shouldBe 0
  })

}
