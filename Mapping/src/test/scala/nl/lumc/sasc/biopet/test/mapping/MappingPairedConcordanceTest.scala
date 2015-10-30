package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.mapping.reference.ReferencePairedTemplate
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.testng.annotations.{BeforeClass, Test}

import scala.io.Source

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
  def numReadAlignedMapped: Unit = {
    val biopetFlagstat = summary \ "samples" \ sampleId.get \ "bammetrics" \ "stats" \ "biopet_flagstat"

    // These values derive from a reference bam
    biopetFlagstat \ "Mapped" shouldBe JInt(BigInt(100000))
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def numReadAlignedUnMapped: Unit = {
    val biopetFlagstat = summary \ "samples" \ sampleId.get \ "bammetrics" \ "stats" \ "biopet_flagstat"

    // These values derive from a reference bam
    biopetFlagstat \ "Unmapped" shouldBe JInt(BigInt(100000))
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def numReadInput: Unit = {
    val biopetFlagstat = summary \ "samples" \ sampleId.get \ "bammetrics" \ "stats" \ "biopet_flagstat"

    biopetFlagstat \ "All" shouldBe JInt(BigInt(1000000))
  }

  @BeforeClass
  def loadTestSet: Unit = {

    println(s"Loading ${testSetName}")
    // loading the reference bam file for comparison
    this.referenceBam = new File(Biopet.fixtureDir, s"${testSetName}.bam")
    this.summaryRefFile = new File(Biopet.fixtureDir + File.separator + s"${testSetName}.json")
  }
//
//  @Test
//  def containsReadID: Unit = {
//    val testSet = loadBamToList(finalBamFile)
//
//  }

  /*
  * Extracts alignment records and extracts some properties to test on
//  * */
//  def loadBamToList(bamfile: File): Iterable[List[Any]] = {
//    val inputSam = SamReaderFactory.makeDefault.open(bamfile)
//    val samIterator = inputSam.iterator().toIterable
//
//    val testSet: Iterable[List[Any]] = for (read <- samIterator) yield List(read.getReadName, read.getCigarString, read.getAlignmentStart, read.getReadNegativeStrandFlag)
//
//    inputSam.close()
//    testSet
//  }

}

class MappingConcordanceTestDNA extends MappingPairedConcordanceTest("dna-set")
//class MappingConcordanceTestRNA extends MappingPairedConcordanceTest("rna-set")




trait MappingWiggleConcordance extends MappingPairedWigTest {
  override def functionalTest = true

  @Test()
  def similarWiggleTrack: Unit = {
    // Loading the reference wiggle (A)
    val referenceWiggle = new File(Biopet.fixtureDir, "mapping/default.wig")

    val tableA = loadWiggleFile(referenceWiggle)
    val tableB = loadWiggleFile(finalWigFile)

    tableB.size shouldBe tableA.size

    // function: do a correlation computation for file A and B
    val correlationScore: Double = pearsonScore( tableA.values.toList, tableB.values.toList ).getOrElse(0.0)

    // in the comparison we allow 1 percent difference.
    correlationScore should be > 0.99

  }

  def finalWigFile: File = new File(outputDir, s"${sampleId.get}-${libId.get}.final.bam.wig")

  /**
   *
   * Taken from http://alvinalexander.com/scala/scala-pearson-correlation-score-algorithm-programming-collective-intelligence
   * @param a List containing Doubles
   * @param b List containing Doubles
   * @return
   */
  def pearsonScore(a: List[Double], b: List[Double]): Option[Double] = {

    assert(a.size == b.size, "Sizes of both Maps are not equal")
    val n = a.size
    // add up all the preferences
    val sum1 = a.sum
    val sum2 = b.sum

    // sum up the squares
    val sum1Sq = a.foldLeft(0.0)(_ + Math.pow(_, 2))
    val sum2Sq = b.foldLeft(0.0)(_ + Math.pow(_, 2))

    // sum up the products
    val pSum = (a.view.zipWithIndex foldLeft 0.0) {
      case (acc, (value,index)) => acc + (value * b(index))
    }

    //  // calculate the pearson score
    val numerator = pSum - (sum1*sum2/n)
    val denominator = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/n) * (sum2Sq-Math.pow(sum2,2)/n))
    if (denominator == 0) None else Some(numerator/denominator)
  }

  /**
   * Wigglefile loader
   *
   * @param wiggleFile Path to wiggleFile (as File-object)
   **/
  def loadWiggleFile( wiggleFile: File ): Map[String,Double] = {
    val reader = Source.fromFile(wiggleFile)
    val lines = reader.getLines()
      .map(line => parseWiggleLine(line))
      .filterNot( _ == Map.empty)
    lines
  }

  /**
   * Converts wiggle line to a Map(binstart -> value)
   *
   * @param wiggleLine Raw line taken from the wiggle
   **/
  def parseWiggleLine(wiggleLine: String): Map[String, Double] = {
    var result: Map[String, Double] = Map.empty

    // line could start with "track"
    // line could start with variableStep

    if (wiggleLine.startsWith("track") || wiggleLine.startsWith("variableStep")) {
      result = Map.empty
    } else {
      val values: Array[String] = wiggleLine.stripLineEnd.split("\t")
      result = Map(values(0) -> values(1).toDouble)
    }
    result
  }
}

class MappingWiggleBWA extends ReferencePairedTemplate("bwa-mem") with MappingWiggleConcordance
//class MappingWiggleStar extends ReferencePairedTemplate("star") with MappingWiggleConcordance
//class MappingWiggleBowtie extends ReferencePairedTemplate("bowtie") with MappingWiggleConcordance