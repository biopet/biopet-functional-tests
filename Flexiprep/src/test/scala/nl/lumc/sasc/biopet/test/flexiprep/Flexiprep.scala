package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ SummaryPipeline, Pipeline }
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

  def skipClip = Option(false)

  def skipTrim = Option(false)

  def keepQcFastqFiles = true

  def args = Seq("-sample", sampleId, "-library", libId, "-cv", "output_dir=" + outputDir) ++
    r1.collect { case r1 => Seq("-R1", r1.getAbsolutePath) }.getOrElse(Seq()) ++
    r2.collect { case r2 => Seq("-R2", r2.getAbsolutePath) }.getOrElse(Seq()) ++
    (if (keepQcFastqFiles) Seq("-cv", "keepQcFastqFiles=true") else Seq("-cv", "keepQcFastqFiles=false")) ++
    (skipClip match {
      case Some(true)  => Seq("-cv", "skip_clip=true")
      case Some(false) => Seq("-cv", "skip_clip=false")
      case _           => Seq()
    }) ++ (skipTrim match {
      case Some(true)  => Seq("-cv", "skip_trim=true")
      case Some(false) => Seq("-cv", "skip_trim=false")
      case _           => Seq()
    })
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
  def testInputR1File() = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "input_R1"
    validateSummaryFile(summaryFile, file = r1, md5 = Some(md5SumInputR1))
    assert(r1.get.exists(), "Input file R1 does not exits anymore")
    assert(calcMd5(r1.get) == md5SumInputR1)
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputR2File() = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "input_R2"
    if (r2.isDefined) {
      validateSummaryFile(summaryFile, file = r2, md5 = md5SumInputR2)
      assert(r2.get.exists(), "Input file R2 does not exits anymore")
      md5SumInputR2.foreach(md5 => assert(calcMd5(r2.get) == md5))
    } else summaryFile shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR1File() = {
    val outputFile = new File(outputDir, s"$sampleId-$libId.R1.qc${if (r2.isDefined) ".sync" else ""}.fq.gz")
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
  def testOutputR2File() = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "output_R2"
    if (r2.isDefined) {
      val outputFile = new File(outputDir, s"$sampleId-$libId.R2.qc.sync.fq.gz")
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
  def testSeqstatR1(): Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R1"
    assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
    //TODO: check stats
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR2(): Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R2"
    if (r2.isDefined) {
      assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
      //TODO: check stats
    } else seqstat shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR1Qc(): Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R1_qc"
    assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
    //TODO: check stats
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR2Qc(): Unit = {
    val seqstat = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "seqstat_R2_qc"
    if (r2.isDefined) {
      assert(seqstat.isInstanceOf[JObject], s"summaryFile if not a JObject: $seqstat")
      //TODO: check stats
    } else seqstat shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testClippingR1(): Unit = {
    val clipping = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "clipping_R1"
    skipClip match {
      case Some(false) | None =>
        assert(clipping.isInstanceOf[JObject], s"summary if not a JObject: $clipping")
      //TODO: check stats
      case _ => clipping shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testClippingR2(): Unit = {
    val clipping = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "clipping_R2"
    skipClip match {
      case Some(false) | None if r2.isDefined =>
        assert(clipping.isInstanceOf[JObject], s"summary if not a JObject: $clipping")
      //TODO: check stats
      case _ => clipping shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testTrimmingR1(): Unit = {
    val trimming = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "trimming_R1"
    skipTrim match {
      case Some(false) | None =>
        assert(trimming.isInstanceOf[JObject], s"summary if not a JObject: $trimming")
      //TODO: check stats
      case _ => trimming shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testTrimmingR2(): Unit = {
    val trimming = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "trimming_R2"
    skipTrim match {
      case Some(false) | None if r2.isDefined =>
        assert(trimming.isInstanceOf[JObject], s"summary if not a JObject: $trimming")
      //TODO: check stats
      case _ => trimming shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFastqSync(): Unit = {
    val syncing = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "fastq_sync"
    if (r2.isDefined) {
      assert(syncing.isInstanceOf[JObject], s"summary if not a JObject: $syncing")
      //TODO: check stats
    } else syncing shouldBe JNothing
  }

}
