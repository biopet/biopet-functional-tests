package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.aligners._
import nl.lumc.sasc.biopet.test.utils._
import org.json4s._
import org.testng.annotations.Test

import scala.io.Source

/**
 * Created by pjvan_thof on 10/7/15.
 */
trait MappingPaired extends MappingSingle {
  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))

  override def r2 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))

  @Test(dependsOnGroups = Array("parseSummary"))
  def seqstatR2: Unit = {
    val seqstat = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep" \ "stats" \ "seqstat_R2"
    if (skipFlexiprep == Some(true)) seqstat shouldBe JNothing
    else {
      seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
      seqstat \ "bases" \ "num_total" shouldBe JInt(BigInt(1000000))
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def seqstatR2Qc: Unit = {
    val seqstat = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep" \ "stats" \ "seqstat_R2_qc"
    if (skipFlexiprep == Some(true)) seqstat shouldBe JNothing
    else {
      seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
      seqstat \ "bases" \ "num_total" shouldBe JInt(BigInt(1000000))
    }
  }

  /** JSON paths for summary. */
  protected val bamMetricsPath = (sampleId, libId) match {
    case (Some(sid), Some(lid)) => Seq("samples", sid, "libraries", lid, "bammetrics")
    case otherwise              => Seq()
  }
  protected val statsPath = bamMetricsPath :+ "stats"
}

trait MappingStatsBwaMem extends MappingPaired with BwaMem {
  // add metrics test only when this is turned on in the pipeline
  if (skipMetrics != Some(true)) {
    addStatsTest(wgsGroup, "metrics" :: "MEDIAN_COVERAGE" :: Nil, _ shouldEqual 63 +- 2)
    addStatsTest(wgsGroup, "metrics" :: "MEAN_COVERAGE" :: Nil, _ shouldEqual 63.0 +- 2.0)
    addStatsTest(wgsGroup, "metrics" :: "SD_COVERAGE" :: Nil, _ shouldEqual 11.0 +- 2.0)

    addStatsTest(bamstatsGroup, "flagstats" :: "All" :: Nil, _ shouldEqual 20000)
    addStatsTest(bamstatsGroup, "flagstats" :: "Mapped" :: Nil, _ shouldEqual 19800 +- 200)
    addStatsTest(bamstatsGroup, "flagstats" :: "ProperPair" :: Nil, _ shouldEqual 20000 +- 100)
    addStatsTest(bamstatsGroup, "flagstats" :: "ReadPaired" :: Nil, _ shouldEqual 20000)
    addStatsTest(bamstatsGroup, "flagstats" :: "FirstOfPair" :: Nil, _ shouldEqual 10000)
    addStatsTest(bamstatsGroup, "flagstats" :: "SecondOfPair" :: Nil, _ shouldEqual 10000)
    addStatsTest(bamstatsGroup, "flagstats" :: "Duplicates" :: Nil, _.asInstanceOf[Int] should be <= 10)
    addStatsTest(bamstatsGroup, "flagstats" :: "MAPQ>30" :: Nil, _ shouldEqual 19750 +- 250)
    addStatsTest(bamstatsGroup, "flagstats" :: "MAPQ>40" :: Nil, _ shouldEqual 19750 +- 250)
    addStatsTest(bamstatsGroup, "flagstats" :: "MAPQ>50" :: Nil, _ shouldEqual 19750 +- 250)

    addSettingsTest(insertsizeGroup, "metrics" :: "READ_PAIRS" :: Nil, _ shouldEqual 10000 +- 50)
    addSettingsTest(insertsizeGroup, "metrics" :: "MEDIAN_INSERT_SIZE" :: Nil, _ shouldEqual 500 +- 15)
    addSettingsTest(insertsizeGroup, "metrics" :: "MEAN_INSERT_SIZE" :: Nil, _ shouldEqual 500.0 +- 15.0)
    addSettingsTest(insertsizeGroup, "metrics" :: "PAIR_ORIENTATION" :: Nil, _ shouldBe "FR")
  }

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
    Source.fromFile(wiggleFile)
      .getLines()
      .filterNot(_.startsWith("track"))
      .filterNot(_.startsWith("variableStep"))
      .filterNot(_.startsWith("fixedStep"))
      .map(_.stripLineEnd.split("\t").last.toDouble)
  }

  @Test()
  def similarWiggleTrack: Unit = {
    // Loading the reference wiggle (A)
    val referenceWiggle = Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "wgs1-testlib.final.bam.wig")

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

class MappingPairedBowtieTest extends MappingPaired with Bowtie

class MappingPairedBowtie2Test extends MappingPaired with Bowtie2

class MappingPairedGsnapTest extends MappingPaired with Gsnap

class MappingPairedTophatTest extends MappingPaired with Tophat

class MappingPairedHisat2Test extends MappingPaired with Hisat2
