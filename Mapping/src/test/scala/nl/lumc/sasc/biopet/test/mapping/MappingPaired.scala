package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import org.json4s._
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 10/7/15.
 */
trait MappingPaired extends MappingSingle {
  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))

  @Test(dependsOnGroups = Array("parseSummary"))
  def seqstatR2: Unit = {
    val seqstat = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep" \ "stats" \ "seqstat_R2"
    if (skipFlexiprep.contains(true)) summary shouldBe JNothing
    else seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def seqstatR2Qc: Unit = {
    val seqstat = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep" \ "stats" \ "seqstat_R2_qc"
    if (skipFlexiprep.contains(true)) summary shouldBe JNothing
    else seqstat \ "reads" \ "num_total" shouldBe JInt(BigInt(10000))
  }
}

class MappingPairedDefaultTest extends MappingPaired

class MappingPairedNoSkipTest extends MappingPaired {
  override def skipFlexiprep = Some(false)
  override def skipMetrics = Some(false)
  override def skipMarkDuplicates = Some(false)
}

class MappingSkipTest extends MappingPaired {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)
}

class MappingPairedAutoChunkTest extends MappingPaired {
  override def chunking = Some(true)
  override def chunksize = Some(110000)
}

class MappingPairedChunkTest extends MappingPaired {
  override def chunking = Some(true)
  override def numberChunks = Some(4)
}
