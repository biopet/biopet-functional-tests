package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
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
    if (skipFlexiprep.contains(true)) seqstat shouldBe JNothing
    else {
      seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
      seqstat \ "bases" \ "num_total" shouldBe JInt(BigInt(1000000))
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def seqstatR2Qc: Unit = {
    val seqstat = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep" \ "stats" \ "seqstat_R2_qc"
    if (skipFlexiprep.contains(true)) seqstat shouldBe JNothing
    else {
      seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
      seqstat \ "bases" \ "num_total" shouldBe JInt(BigInt(1000000))
    }
  }

  /** JSON paths for summary. */
  protected val bamMetricsPath = (sampleId, libId) match {
    case (Some(sid), Some(lid)) => Seq("samples", sid, "libraries", lid, "bammetrics")
    case otherwise => Seq()
  }
  protected val statsPath = bamMetricsPath :+ "stats"

  // add metrics test only when this is turned on in the pipeline
  if (skipMetrics.contains(false)) {

    addSummaryTest(statsPath :+ "wgs" :+ "metrics",
      Seq(
        x => (x \ "MEDIAN_COVERAGE").extract[Int] shouldEqual 63 +- 2,
        x => (x \ "MEAN_COVERAGE").extract[Double] shouldEqual 63.0 +- 2.0,
        x => (x \ "SD_COVERAGE").extract[Double] shouldEqual 11.0 +- 2.0
      ))

    addSummaryTest(statsPath :+ "biopet_flagstat",
      Seq(
        _ \ "All" should haveValue(20000),
        x => (x \ "Mapped").extract[Int] shouldEqual 19800 +- 200,
        x => (x \ "ProperPair").extract[Int] shouldEqual 20000 +- 100,
        _ \ "ReadPaired" should haveValue(20000),
        _ \ "FirstOfPair" should haveValue(10000),
        _ \ "SecondOfPair" should haveValue(10000),
        x => (x \ "Duplicates").extract[Int] should be <= 10,
        x => (x \ "MAPQ>30").extract[Int] shouldEqual 19750 +- 250,
        x => (x \ "MAPQ>40").extract[Int] shouldEqual 19750 +- 250,
        x => (x \ "MAPQ>50").extract[Int] shouldEqual 19750 +- 250
      ))

    addSummaryTest(statsPath :+ "CollectInsertSizeMetrics" :+ "metrics",
      Seq(
        x => (x \ "READ_PAIRS").extract[Int] shouldEqual 10000 +- 50,
        x => (x \ "MEDIAN_INSERT_SIZE").extract[Int] shouldEqual 500 +- 15,
        x => (x \ "MEAN_INSERT_SIZE").extract[Double] shouldEqual 500.0 +- 15.0,
        _ \ "PAIR_ORIENTATION" should haveValue("FR")
      ))
  }


}

class MappingPairedDefaultTest extends MappingPaired

class MappingPairedNoSkipTest extends MappingPaired {
  override def skipFlexiprep = Some(false)

  override def skipMetrics = Some(false)

  override def skipMarkDuplicates = Some(false)

  override def generateWig = Some(true)
}

class MappingPairedWigTest extends MappingPaired {
  override def generateWig = Some(true)

  /**
    * Wigglefile loader
    *
    * @param wiggleFile Path to wiggleFile (as File-object)
    */
  def loadWiggleFile(wiggleFile: File): List[Double] = {
    val reader = Source.fromFile(wiggleFile)
    val wiggleValue = reader.getLines()
      .filterNot(_.startsWith("track"))
      .filterNot(_.startsWith("variableStep"))
      .filterNot(_.startsWith("fixedStep"))
      .map( _.stripLineEnd.split("\t").last.toDouble )
      .toList
    reader.close()
    wiggleValue
  }

  @Test()
  def similarWiggleTrack: Unit = {
    // Loading the reference wiggle (A)
    val referenceWiggle = Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "wgs1-testlib.final.bam.wig")

    val tableA = loadWiggleFile(referenceWiggle)
    val tableB = loadWiggleFile(finalWigFile)

    tableB.size shouldBe tableA.size

    val correlationScore: Double = pearsonScore(tableA, tableB).getOrElse(0.0)

    // in the comparison we allow 1 percent difference.
    correlationScore should be > 0.99
  }

}

class MappingPairedSkipTest extends MappingPaired {
  override def skipFlexiprep = Some(true)

  override def skipMetrics = Some(true)

  override def skipMarkDuplicates = Some(true)

  override def generateWig = Some(true)
}

class MappingPairedAutoChunkTest extends MappingPaired {
  override def configChunking = Some(true)

  override def configChunksize = Some(110000)
}

class MappingPairedChunkTest extends MappingPaired {
  override def configChunking = Some(true)

  override def configNumberChunks = Some(4)
}

class MappingPairedForceNoChunkTest extends MappingPaired {
  override def configChunking = Some(false)

  override def configNumberChunks = Some(4)
}

class MappingPairedChunkMetricsTest extends MappingPaired {
  override def chunkMetrics = Some(true)

  override def configChunking = Some(true)

  override def configNumberChunks = Some(4)
}

