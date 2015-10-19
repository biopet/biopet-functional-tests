package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ SummaryPipeline, Pipeline }
import nl.lumc.sasc.biopet.test.Pipeline._
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

  def keepQcFastqFiles: Option[Boolean] = None

  def args = Seq("-sample", sampleId, "-library", libId) ++
    cmdArg("-R1", r1) ++ cmdArg("-R2", r2) ++
    cmdConfig("keepQcFastqFiles", keepQcFastqFiles) ++
    cmdConfig("skip_clip", skipClip) ++
    cmdConfig("skip_trim", skipTrim)
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

  addExecutable(Executable("fastqc", Some(""".+""".r)))
  addExecutable(Executable("seqstat", Some(""".+""".r)))
  addExecutable(Executable("seqtkseq", Some(""".+""".r)))
  if (r2.isDefined) addExecutable(Executable("fastqsync", Some(""".+""".r)))
  if (!skipTrim.contains(true)) addExecutable(Executable("sickle", Some(""".+""".r)))
  else addNotExecutable("sickle")
  if (skipClip.contains(true)) addNotExecutable("cutadapt")

  override def summaryRoot = summaryLibrary(sampleId, libId)

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputR1File() = {
    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "input_R1"
    validateSummaryFile(summaryFile, file = r1, md5 = Some(md5SumInputR1))
    assert(r1.get.exists(), "Input file R1 does not exits anymore")
    assert(calcMd5(r1.get) == md5SumInputR1)
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputR2File() = {
    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "input_R2"
    if (r2.isDefined) {
      validateSummaryFile(summaryFile, file = r2, md5 = md5SumInputR2)
      assert(r2.get.exists(), "Input file R2 does not exits anymore")
      md5SumInputR2.foreach(md5 => assert(calcMd5(r2.get) == md5))
    } else summaryFile shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR1File() = {
    val outputFile = new File(outputDir, s"$sampleId-$libId.R1.qc${if (r2.isDefined) ".sync" else ""}.fq.gz")
    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "output_R1"
    validateSummaryFile(summaryFile)
    (summaryFile \ "path").extract[String] shouldBe outputFile.getAbsolutePath

    if (!keepQcFastqFiles.contains(false)) {
      assert(outputFile.exists(), "Output file R1 should exist while keepQcFastqFiles=true")
      md5SumOutputR1.foreach(calcMd5Unzipped(outputFile) shouldBe _)
      calcMd5(outputFile) shouldBe (summaryFile \ "md5").extract[String]
    } else assert(!outputFile.exists(), "Output file R1 should not exist while keepQcFastqFiles=false")
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR2File() = {
    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "output_R2"
    if (r2.isDefined) {
      val outputFile = new File(outputDir, s"$sampleId-$libId.R2.qc.sync.fq.gz")
      validateSummaryFile(summaryFile)
      (summaryFile \ "path").extract[String] shouldBe outputFile.getAbsolutePath

      if (!keepQcFastqFiles.contains(false)) {
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
  def testSettings(): Unit = {
    val settings = summaryRoot \ "flexiprep" \ "settings"
    settings shouldBe a[JObject]

    settings \ "skip_trim" shouldBe JBool(skipTrim.getOrElse(false))
    settings \ "skip_clip" shouldBe JBool(skipClip.getOrElse(false))
    settings \ "paired" shouldBe JBool(r2.isDefined)
  }

  @Test(dependsOnGroups = Array("parseSummary"), groups = Array("summaryFastqcR1"))
  def testFastqcR1(): Unit = {
    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R1"
    fastqc shouldBe a[JObject]
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFastqcR2(): Unit = {
    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R2"
    if (r2.isDefined) {
      fastqc shouldBe a[JObject]
      //TODO: check stats
    } else fastqc shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFastqcR1Qc(): Unit = {
    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R1_qc"
    fastqc shouldBe a[JObject]
    //TODO: check stats
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFastqcR2Qc(): Unit = {
    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R2_qc"
    if (r2.isDefined) {
      fastqc shouldBe a[JObject]
      //TODO: check stats
    } else fastqc shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR1(): Unit = {
    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R1"
    seqstat shouldBe a[JObject]
    //TODO: check stats
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR2(): Unit = {
    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R2"
    if (r2.isDefined) {
      seqstat shouldBe a[JObject]
      //TODO: check stats
    } else seqstat shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR1Qc(): Unit = {
    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R1_qc"
    seqstat shouldBe a[JObject]
    //TODO: check stats
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSeqstatR2Qc(): Unit = {
    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R2_qc"
    if (r2.isDefined) {
      seqstat shouldBe a[JObject]
      //TODO: check stats
    } else seqstat shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testClippingR1(): Unit = {
    val clipping = summaryRoot \ "flexiprep" \ "stats" \ "clipping_R1"
    val adapters = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R1" \ "adapters"
    skipClip match {
      case Some(false) | None =>
        if (adapters.asInstanceOf[JObject].values.nonEmpty) clipping shouldBe a[JObject]
        else clipping shouldBe JNothing
      //TODO: check stats
      case _ => clipping shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testClippingR2(): Unit = {
    val clipping = summaryRoot \ "flexiprep" \ "stats" \ "clipping_R2"
    val adapters = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R2" \ "adapters"
    skipClip match {
      case Some(false) | None if r2.isDefined =>
        if (adapters.asInstanceOf[JObject].values.nonEmpty) clipping shouldBe a[JObject]
        else clipping shouldBe JNothing
      //TODO: check stats
      case _ => clipping shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testTrimmingR1(): Unit = {
    val trimming = summaryRoot \ "flexiprep" \ "stats" \ "trimming_R1"
    skipTrim match {
      case Some(false) | None =>
        trimming shouldBe a[JObject]
      //TODO: check stats
      case _ => trimming shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testTrimmingR2(): Unit = {
    val trimming = summaryRoot \ "flexiprep" \ "stats" \ "trimming_R2"
    skipTrim match {
      case Some(false) | None if r2.isDefined =>
        trimming shouldBe a[JObject]
      //TODO: check stats
      case _ => trimming shouldBe JNothing
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFastqSync(): Unit = {
    val syncing = summaryRoot \ "flexiprep" \ "stats" \ "fastq_sync"
    if (r2.isDefined) {
      syncing shouldBe a[JObject]
      //TODO: check stats
    } else syncing shouldBe JNothing
  }

}
