package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.{ TestReference, Biopet }
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

class MappingSingleDefaultTest extends MappingSingle

class MappingSingleNoSkipTest extends MappingSingle {
  override def skipFlexiprep = Some(false)
  override def skipMetrics = Some(false)
  override def skipMarkDuplicates = Some(false)
}

class MappingSingleSkipTest extends MappingSingle {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)
}

class MappingSingleAutoChunkTest extends MappingPaired {
  override def chunking = Some(true)
  override def chunksize = Some(110000)
}

class MappingSingleChunkTest extends MappingPaired {
  override def chunking = Some(true)
  override def numberChunks = Some(4)
}
