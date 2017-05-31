package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.Pipeline.cmdConfig
import nl.lumc.sasc.biopet.test.aligners._
import nl.lumc.sasc.biopet.test.utils._
import org.testng.annotations.Test

import scala.io.Source

/**
  * Created by pjvan_thof on 10/7/15.
  */
trait MappingPaired extends MappingSingle {
  override def r1 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))

  override def r2 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))

  addStatsTest(flexiprepGroup.copy(module = "seqstat_R2"),
               "reads" :: "num_total" :: Nil,
               _ shouldBe 10000,
               skipFlexiprep != Some(true))
  addStatsTest(flexiprepGroup.copy(module = "seqstat_R2"),
               "bases" :: "num_total" :: Nil,
               _ shouldBe 1000000,
               skipFlexiprep != Some(true))
  addStatsTest(flexiprepGroup.copy(module = "seqstat_R2_qc"),
               "reads" :: "num_total" :: Nil,
               _ shouldBe 10000,
               skipFlexiprep != Some(true))
  addStatsTest(flexiprepGroup.copy(module = "seqstat_R2_qc"),
               "bases" :: "num_total" :: Nil,
               _ shouldBe 1000000,
               skipFlexiprep != Some(true))
}

trait MappingStatsBwaMem extends MappingPaired with BwaMem {
  // add metrics test only when this is turned on in the pipeline
  addStatsTest(wgsGroup,
               "metrics" :: "MEDIAN_COVERAGE" :: Nil,
               _.toString.toLong shouldEqual 63L +- 2L,
               skipMetrics != Some(true))
  addStatsTest(wgsGroup,
               "metrics" :: "MEAN_COVERAGE" :: Nil,
               _.toString.toDouble shouldEqual 63.0 +- 2.0,
               skipMetrics != Some(true))
  addStatsTest(wgsGroup,
               "metrics" :: "SD_COVERAGE" :: Nil,
               _.toString.toDouble shouldEqual 11.0 +- 2.0,
               skipMetrics != Some(true))

  addStatsTest(bamstatsGroup,
               "flagstats" :: "All" :: Nil,
               _.toString.toLong shouldEqual 20000L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "Mapped" :: Nil,
               _.toString.toLong shouldEqual 19800L +- 200L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "ProperPair" :: Nil,
               _.toString.toLong shouldEqual 20000L +- 100L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "ReadPaired" :: Nil,
               _.toString.toLong shouldEqual 20000L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "FirstOfPair" :: Nil,
               _.toString.toLong shouldEqual 10000L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "SecondOfPair" :: Nil,
               _ shouldEqual 10000L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "Duplicates" :: Nil,
               _.toString.toLong should be <= 10L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "MAPQ>30" :: Nil,
               _.toString.toLong shouldEqual 19750L +- 250L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "MAPQ>40" :: Nil,
               _.toString.toLong shouldEqual 19750L +- 250L,
               skipMetrics != Some(true))
  addStatsTest(bamstatsGroup,
               "flagstats" :: "MAPQ>50" :: Nil,
               _.toString.toLong shouldEqual 19750L +- 250L,
               skipMetrics != Some(true))

  addStatsTest(insertsizeGroup,
               "metrics" :: "READ_PAIRS" :: Nil,
               _.toString.toLong shouldEqual 10000L +- 50L,
               skipMetrics != Some(true))
  addStatsTest(insertsizeGroup,
               "metrics" :: "MEDIAN_INSERT_SIZE" :: Nil,
               _.toString.toLong shouldEqual 500L +- 15L,
               skipMetrics != Some(true))
  addStatsTest(insertsizeGroup,
               "metrics" :: "MEAN_INSERT_SIZE" :: Nil,
               _.toString.toDouble shouldEqual 500.0 +- 15.0,
               skipMetrics != Some(true))
  addStatsTest(insertsizeGroup,
               "metrics" :: "PAIR_ORIENTATION" :: Nil,
               _ shouldBe "FR",
               skipMetrics != Some(true))
}

class MappingPairedDefaultTest extends MappingPaired with MappingStatsBwaMem

class MappingPairedNoSkipTest extends MappingPaired with MappingStatsBwaMem {
  override def skipFlexiprep = Some(false)

  override def skipMetrics = Some(false)

  override def skipMarkDuplicates = Some(false)

  override def generateWig = Some(true)
}

class MappingPairedWigTest extends MappingPaired with MappingStatsBwaMem {
  override def generateWig = Some(true)

  /**
    * Wigglefile loader
    *
    * @param wiggleFile Path to wiggleFile (as File-object)
    */
  def loadWiggleFile(wiggleFile: File): Iterator[Double] = {
    Source
      .fromFile(wiggleFile)
      .getLines()
      .filterNot(_.startsWith("track"))
      .filterNot(_.startsWith("variableStep"))
      .filterNot(_.startsWith("fixedStep"))
      .map(_.stripLineEnd.split("\t").last.toDouble)
  }

  @Test()
  def similarWiggleTrack: Unit = {
    // Loading the reference wiggle (A)
    val referenceWiggle = Biopet.fixtureFile(
      "samples" + File.separator + "wgs1" + File.separator + "wgs1-testlib.final.bam.wig")

    val tableA = loadWiggleFile(referenceWiggle)
    val tableB = loadWiggleFile(finalWigFile)

    val correlationScore: Double = pearsonScore(tableA, tableB).getOrElse(0.0)

    // in the comparison we allow 1 percent difference.
    correlationScore should be >= 0.99
  }

}

class MappingPairedSkipTest extends MappingPaired with MappingStatsBwaMem {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)
  override def generateWig = Some(true)
}

class MappingPairedReadgroupTest extends MappingPaired with MappingStatsBwaMem {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)
  override def generateWig = Some(true)
  override def readgroupLibrary = Some("random_lib")
}

class MappingPairedAutoChunkTest extends MappingPaired with MappingStatsBwaMem {
  override def configChunking = Some(true)

  override def configChunksize = Some(110000)
}

class MappingPairedChunkTest extends MappingPaired with MappingStatsBwaMem {
  override def configChunking = Some(true)

  override def configNumberChunks = Some(4)
}

class MappingPairedForceNoChunkTest extends MappingPaired with MappingStatsBwaMem {
  override def configChunking = Some(false)

  override def configNumberChunks = Some(4)
}

class MappingPairedChunkMetricsTest extends MappingPaired with MappingStatsBwaMem {
  override def chunkMetrics = Some(true)

  override def configChunking = Some(true)

  override def configNumberChunks = Some(4)
}

class MappingPairedBwaMemTest extends MappingPaired with MappingStatsBwaMem

class MappingPairedBowtieTest extends MappingPaired with Bowtie {

  override def args = super.args ++ cmdConfig("maxins", 700)
}

class MappingPairedBowtie2Test extends MappingPaired with Bowtie2

class MappingPairedGsnapTest extends MappingPaired with Gsnap

class MappingPairedTophatTest extends MappingPaired with Tophat {

  override def args = super.args ++ cmdConfig("mate_inner_dist", 300)
}

class MappingPairedHisat2Test extends MappingPaired with Hisat2
