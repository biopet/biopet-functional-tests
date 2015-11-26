package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.aligners._
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.Biopet
import org.json4s._
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 10/7/15.
 */
trait MappingSingle extends MappingSuccess with TestReference {
  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))

  @Test(dependsOnGroups = Array("parseSummary"))
  def seqstatR1: Unit = {
    val seqstat = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep" \ "stats" \ "seqstat_R1"
    if (skipFlexiprep.contains(true)) seqstat shouldBe JNothing
    else {
      seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
      seqstat \ "bases" \ "num_total" shouldBe JInt(BigInt(1000000))
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def seqstatR1Qc: Unit = {
    val seqstat = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep" \ "stats" \ "seqstat_R1_qc"
    if (skipFlexiprep.contains(true)) seqstat shouldBe JNothing
    else {
      seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
      seqstat \ "bases" \ "num_total" shouldBe JInt(BigInt(1000000))
    }
  }
}

class MappingSingleDefaultTest extends MappingSingle with BwaMem

class MappingSingleNoSkipTest extends MappingSingle with BwaMem {
  override def skipFlexiprep = Some(false)
  override def skipMetrics = Some(false)
  override def skipMarkDuplicates = Some(false)
  override def generateWig = Some(true)
}

class MappingSingleWigTest extends MappingSingle with BwaMem {
  override def generateWig = Some(true)
}

class MappingSingleSkipTest extends MappingSingle with BwaMem {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)
  override def generateWig = Some(true)
}

class MappingSingleAutoChunkTest extends MappingPaired with BwaMem {
  override def configChunking = Some(true)
  override def configChunksize = Some(110000)
}

class MappingSingleChunkTest extends MappingPaired with BwaMem {
  override def configChunking = Some(true)
  override def configNumberChunks = Some(4)
}

class MappingSingleForceNoChunkTest extends MappingPaired with BwaMem {
  override def configChunking = Some(false)
  override def configNumberChunks = Some(4)
}

class MappingSingleChunkMetricsTest extends MappingPaired with BwaMem {
  override def chunkMetrics = Some(true)
  override def configChunking = Some(true)
  override def configNumberChunks = Some(4)
}

class MappingSingleBwaMemTest extends MappingSingle with BwaMem

class MappingSingleBowtieTest extends MappingSingle with Bowtie

class MappingSingleBowtie2Test extends MappingSingle with Bowtie2

class MappingSingleGsnapTest extends MappingSingle with Gsnap

class MappingSingleTophatTest extends MappingSingle with Tophat

class MappingSingleStarTest extends MappingSingle with Star
