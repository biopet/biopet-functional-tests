package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.utils._
import nl.lumc.sasc.biopet.test.{Pipeline, SummaryPipeline}
import org.json4s._
import org.testng.annotations.Test

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

  def inputEncodingR1 = "sanger"

  def inputEncodingR2 = "sanger"

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
  else addNotHavingExecutable("sickle")
  if (skipClip.contains(true)) addNotHavingExecutable("cutadapt")

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

/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepSingle extends FlexiprepSuccessful {

  /** Input file of this run. */
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  /** JSON paths for summary. */
  protected val flexiprepPath = Seq("samples", sampleId, "libraries", libId, "flexiprep")
  protected val statsPath = flexiprepPath :+ "stats"
  protected val statsFastqcR1Path = statsPath :+ "fastqc_R1"
  protected val statsSeqstatR1Path = statsPath :+ "seqstat_R1"
  protected val statsFastqcR1QcPath = statsPath :+ "fastqc_R1_qc"
  protected val statsSeqstatR1QcPath = statsPath :+ "seqstat_R1_qc"

  addSummaryTest(statsFastqcR1Path :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should haveValue(32.244),
      _ \ "1" \ "median" should haveValue(33),
      _ \ "1" \ "lower_quartile" should haveValue(31),
      _ \ "1" \ "upper_quartile" should haveValue(34),
      _ \ "1" \ "percentile_10th" should haveValue(30),
      _ \ "1" \ "percentile_90th" should haveValue(34),
      _ \ "100" \ "mean" should haveValue(21.984),
      _ \ "100" \ "median" should haveValue(30),
      _ \ "100" \ "lower_quartile" should haveValue(2),
      _ \ "100" \ "upper_quartile" should haveValue(34),
      _ \ "100" \ "percentile_10th" should haveValue(2),
      _ \ "100" \ "percentile_90th" should haveValue(35)))

  addSummaryTest(statsFastqcR1Path :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should haveValue(17.251755265797392),
      _ \ "1" \ "T" should haveValue(11.735205616850552),
      _ \ "1" \ "G" should haveValue(52.35707121364093),
      _ \ "1" \ "C" should haveValue(18.655967903711137),
      _ \ "100" \ "A" should haveValue(26),
      _ \ "100" \ "T" should haveValue(21.9),
      _ \ "100" \ "G" should haveValue(24),
      _ \ "100" \ "C" should haveValue(28.1)))

  addSummaryTest(statsFastqcR1Path :+ "adapters",
    Seq(
      _ \ "TruSeq Adapter, Index 1" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG"),
      _ \ "TruSeq Adapter, Index 18" should haveValue("GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG")))

  addSummaryTest(statsSeqstatR1Path :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(100000),
      _ \ "nucleotides" \ "A" should haveValue(21644),
      _ \ "nucleotides" \ "T" should haveValue(23049),
      _ \ "nucleotides" \ "G" should haveValue(25816),
      _ \ "nucleotides" \ "C" should haveValue(26555),
      _ \ "nucleotides" \ "N" should haveValue(2936),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 16497,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 7264))

  addSummaryTest(statsSeqstatR1Path :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(1000),
      _ \ "num_with_n" should haveValue(175),
      _ \ "len_min" should haveValue(100),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue(inputEncodingR1),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(1000),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(
      _ \ "fastqc_R1" \ "fastqc_data" \ "path" should existAsFile))
}

/** Trait for Flexiprep runs with paired-end inputs. */
trait FlexiprepPaired extends FlexiprepSingle {

  /** Input read pair 2 for this run. */
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))

  /** MD5 checksum of input read pair 2. */
  override def md5SumInputR2 = Some("1560a4cdc87cc8c4b6701e1253d41f93")

  /** JSON paths for summary. */
  protected val statsFastqcR2Path = statsPath :+ "fastqc_R2"
  protected val statsSeqstatR2Path = statsPath :+ "seqstat_R2"
  protected val statsFastqcR2QcPath = statsPath :+ "fastqc_R2_qc"
  protected val statsSeqstatR2QcPath = statsPath :+ "seqstat_R2_qc"

  addSummaryTest(statsFastqcR2Path :+ "per_base_sequence_quality",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "mean" should haveValue(11.351),
      _ \ "1" \ "median" should haveValue(2),
      _ \ "1" \ "lower_quartile" should haveValue(2),
      _ \ "1" \ "upper_quartile" should haveValue(31),
      _ \ "1" \ "percentile_10th" should haveValue(2),
      _ \ "1" \ "percentile_90th" should haveValue(33),
      _ \ "100" \ "mean" should haveValue(5.79),
      _ \ "100" \ "median" should haveValue(2),
      _ \ "100" \ "lower_quartile" should haveValue(2),
      _ \ "100" \ "upper_quartile" should haveValue(2),
      _ \ "100" \ "percentile_10th" should haveValue(2),
      _ \ "100" \ "percentile_90th" should haveValue(26)))

  addSummaryTest(statsFastqcR2Path :+ "per_base_sequence_content",
    Seq(
      _.children.size should be <= 100,
      _ \ "1" \ "A" should haveValue(24.198250728862973),
      _ \ "1" \ "T" should haveValue(5.247813411078718),
      _ \ "1" \ "G" should haveValue(48.68804664723032),
      _ \ "1" \ "C" should haveValue(21.865889212827987),
      _ \ "100" \ "A" should haveValue(27.769784172661872),
      _ \ "100" \ "T" should haveValue(19.568345323741006),
      _ \ "100" \ "G" should haveValue(30.79136690647482),
      _ \ "100" \ "C" should haveValue(21.8705035971223)))

  addSummaryTest(statsFastqcR2Path :+ "adapters", Seq(_.children.size shouldBe 0))

  addSummaryTest(statsSeqstatR2Path :+ "bases",
    Seq(
      _ \ "num_total" should haveValue(100000),
      _ \ "nucleotides" \ "A" should haveValue(13981),
      _ \ "nucleotides" \ "T" should haveValue(11508),
      _ \ "nucleotides" \ "G" should haveValue(16442),
      _ \ "nucleotides" \ "C" should haveValue(14089),
      _ \ "nucleotides" \ "N" should haveValue(43980),
      _ \ "num_qual" shouldBe a[JArray],
      jv => (jv \ "num_qual").extract[List[Int]].apply(41) shouldBe 2288,
      jv => (jv \ "num_qual").extract[List[Int]].apply(2) shouldBe 60383))

  addSummaryTest(statsSeqstatR2Path :+ "reads",
    Seq(
      _ \ "num_total" should haveValue(1000),
      _ \ "num_with_n" should haveValue(769),
      _ \ "len_min" should haveValue(100),
      _ \ "len_max" should haveValue(100),
      _ \ "qual_encoding" should haveValue(inputEncodingR2),
      jv => (jv \ "num_avg_qual_gte").children.size shouldBe 61,
      _ \ "num_avg_qual_gte" \ "0" should haveValue(1000),
      _ \ "num_avg_qual_gte" \ "60" should haveValue(0)))

  addSummaryTest(flexiprepPath :+ "files",
    Seq(
      _ \ "fastqc_R2" \ "fastqc_data" \ "path" should existAsFile))
}

