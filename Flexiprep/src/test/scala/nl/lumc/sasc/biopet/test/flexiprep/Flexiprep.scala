package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, SummaryPipeline, Pipeline }
import org.json4s._
import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

/** Base trait for Flexiprep pipeline run tests. */
trait FlexiprepRun extends Pipeline {

  def pipelineName = "flexiprep"

  def sampleId = "sampleName"

  def libId = "libName"

  def summaryFile = new File(outputDir, s"$sampleId-$libId.qc.summary.json")

  def r1: Option[File] = None

  def r2: Option[File] = None

  def keepQcFastqFiles = true

  def args = Seq("-sample", sampleId, "-library", libId, "-cv", "output_dir=" + outputDir, "-run") ++
    r1.collect { case r1 => Seq("-R1", r1.getAbsolutePath) }.getOrElse(Seq()) ++
    r2.collect { case r2 => Seq("-R2", r2.getAbsolutePath) }.getOrElse(Seq()) ++
    (if (keepQcFastqFiles) Seq("-cv", "keepQcFastqFiles=true") else Seq("-cv", "keepQcFastqFiles=false"))

  def r1Name = r1 collect {
    case r1 => r1.getName
      .stripSuffix(".gz").stripSuffix(".gzip")
      .stripSuffix(".fq").stripSuffix(".fastq")
  }

  def r2Name = r2 collect {
    case r2 => r2.getName
      .stripSuffix(".gz").stripSuffix(".gzip")
      .stripSuffix(".fq").stripSuffix(".fastq")
  }
}

/** Trait representing a successful Flexiprep test group. */
trait FlexiprepSuccessful extends FlexiprepRun with SummaryPipeline {

  logMustNotHave("""Script failed with \d+ total jobs""".r)
  logMustHave("""Script completed successfully with \d+ total jobs""".r)

  def md5SumInputR1: String
  def md5SumInputR2: Option[String] = None

  /** This is the uncompressed md5sum of the output R1 */
  def md5SumOutputR1: Option[String] = None

  /** This is the uncompressed md5sum of the output R2 */
  def md5SumOutputR2: Option[String] = None

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputR1File = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "input_R1"
    validateSummaryFile(summaryFile, file = r1, md5 = Some(md5SumInputR1))
    assert(r1.get.exists(), "Input file R1 does not exits anymore")
    assert(calcMd5(r1.get) == md5SumInputR1)
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputR2File = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "input_R2"
    if (r2.isDefined) {
      validateSummaryFile(summaryFile, file = r2, md5 = md5SumInputR2)
      assert(r2.get.exists(), "Input file R2 does not exits anymore")
      md5SumInputR2.foreach(md5 => assert(calcMd5(r2.get) == md5))
    } else summaryFile shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR1File = {
    val outputFile = new File(outputDir, r1Name.get + s".R1.qc${if (r2.isDefined) ".sync" else ""}.fq.gz")
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "output_R1"
    validateSummaryFile(summaryFile)
    (summaryFile \ "path").extract[String] shouldBe outputFile.getAbsolutePath

    if (keepQcFastqFiles) {
      assert(outputFile.exists(), "Output file R1 should exist while keepQcFastqFiles=true")
      md5SumOutputR1.foreach(calcMd5Unzipped(outputFile) shouldBe _)
      calcMd5(outputFile) shouldBe (summaryFile \ "md5").extract[String]
    } else assert(!outputFile.exists(), "Output file R1 should not exist while keepQcFastqFiles=false")
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR2File = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "output_R2"
    if (r2.isDefined) {
      val outputFile = new File(outputDir, r2Name.get + s".R2.qc.sync.fq.gz")
      validateSummaryFile(summaryFile)
      (summaryFile \ "path").extract[String] shouldBe outputFile.getAbsolutePath

      if (keepQcFastqFiles) {
        assert(outputFile.exists(), "Output file R2 should exist while keepQcFastqFiles=true")
        md5SumOutputR2.foreach(calcMd5Unzipped(outputFile) shouldBe _)
        calcMd5(outputFile) shouldBe (summaryFile \ "md5").extract[String]
      } else assert(!outputFile.exists(), "Output file R2 should not exist while keepQcFastqFiles=false")
    } else {
      summaryFile shouldBe JNothing
      assert(!outputDir.list().exists(x => x.contains(".R2.") || x.contains(".r2.")))
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR1: Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R1"
    assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
    //TODO: check stats
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR2: Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R2"
    if (r2.isDefined) {
      assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
      //TODO: check stats
    } else seqstat shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR1Qc: Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R1_qc"
    assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
    //TODO: check stats
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR2Qc: Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R2_qc"
    if (r2.isDefined) {
      assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
      //TODO: check stats
    } else seqstat shouldBe JNothing
  }

}
