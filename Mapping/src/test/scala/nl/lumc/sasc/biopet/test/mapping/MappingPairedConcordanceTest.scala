package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import htsjdk.samtools.SamReaderFactory
import nl.lumc.sasc.biopet.test.Biopet
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.testng.annotations.{BeforeClass, Test}

import scala.collection.JavaConversions._

/**
 * Created by wyleung on 26-10-15.
 */
class MappingPairedConcordanceTest(testSetName: String) extends MappingPaired {

  var referenceBam: File = _
  var summaryRefFile: File = _
  private var _ref_summary: JValue = _

  override def configChunking = Some(true)

  override def configChunksize = Some(1000)

  override def functionalTest = true

  @Test(groups = Array("parseRefSummary"))
  def parseRefSummary(): Unit = _ref_summary = parse(summaryRefFile)

  @Test(dependsOnGroups = Array("parseSummary"))
  def numReadAligned: Unit = {
    val biopetFlagstat = summary \ "samples" \ sampleId.get \ "bammetrics" \ "stats" \ "biopet_flagstat"

    // These values derive from a reference bam
    biopetFlagstat \ "Mapped" shouldBe JInt(BigInt(100000))
    biopetFlagstat \ "All" shouldBe JInt(BigInt(1000000))
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def readIDmatch: Unit = {
    val biopetFlagstat = summary \ "samples" \ sampleId.get \ "bammetrics" \ "stats" \ "biopet_flagstat"

  }

  @BeforeClass
  def loadTestSet: Unit = {

    println(s"Loading ${testSetName}")
    // loading the reference bam file for comparison
    this.referenceBam = new File(Biopet.fixtureDir, s"${testSetName}.bam")
    this.summaryRefFile = new File(Biopet.fixtureDir + File.separator + s"${testSetName}.json")
  }

  @Test
  def containsReadID: Unit = {
    val testSet = loadBamToList(finalBamFile)

  }

  /*
  * Extracts alignment records and extracts some properties to test on
  * */
  def loadBamToList(bamfile: File): Iterable[List[Any]] = {
    val inputSam = SamReaderFactory.makeDefault.open(bamfile)
    val samIterator = inputSam.iterator().toIterable

    val testSet: Iterable[List[Any]] = for (read <- samIterator) yield List(read.getReadName, read.getCigarString, read.getAlignmentStart, read.getReadNegativeStrandFlag)

    inputSam.close()
    testSet
  }

}

class MappingConcordanceTestDNA extends MappingPairedConcordanceTest("dna-set")

class MappingConcordanceTestRNA extends MappingPairedConcordanceTest("rna-set")

