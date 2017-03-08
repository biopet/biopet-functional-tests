package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test._
import nl.lumc.sasc.biopet.test.utils._
import nl.lumc.sasc.biopet.utils.ConfigUtils
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

  def r1ContainAdapters = true

  def r2ContainAdapters = true

  def inputEncodingR1 = "sanger"

  def inputEncodingR2 = "sanger"

  def keepQcFastqFiles: Option[Boolean] = None

  override def args = super.args ++ Seq("-sample", sampleId, "-library", libId) ++
    cmdArg("-R1", r1) ++ cmdArg("-R2", r2) ++
    cmdConfig("keepQcFastqFiles", keepQcFastqFiles) ++
    cmdConfig("skip_clip", skipClip) ++
    cmdConfig("skip_trim", skipTrim)
}

/** Trait representing a successful Flexiprep test group. */
trait FlexiprepSuccessful extends FlexiprepRun with SummaryPipeline {

  override def summarySchemaUrls = Seq("/schemas/flexiprep.json")

  def md5SumInputR1: String
  def md5SumInputR2: Option[String] = None

  val flexiprepGroup = SummaryGroup("flexiprep", None, Some(sampleId), Some(libId))

  val fastqcR1Group = flexiprepGroup.copy(module = Some("fastqc_R1"))
  val seqstatR1Group = flexiprepGroup.copy(module = Some("seqstat_R1"))
  val fastqcR1QcGroup = flexiprepGroup.copy(module = Some("fastqc_R1_qc"))
  val seqstatR1QcGroup = flexiprepGroup.copy(module = Some("seqstat_R1_qc"))
  
  val fastqcR2Group = flexiprepGroup.copy(module = Some("fastqc_R2"))
  val seqstatR2Group = flexiprepGroup.copy(module = Some("seqstat_R2"))
  val fastqcR2QcGroup = flexiprepGroup.copy(module = Some("fastqc_R1_qc"))
  val seqstatR2QcGroup = flexiprepGroup.copy(module = Some("seqstat_R1_qc"))

  val clippingR1Group = flexiprepGroup.copy(module = Some("clipping_R1"))
  val clippingR2Group = flexiprepGroup.copy(module = Some("clipping_R2"))
  val trimmingR1Group = flexiprepGroup.copy(module = Some("trimming_R1"))
  val trimmingR2Group = flexiprepGroup.copy(module = Some("trimming_R2"))
  val syncGroup = flexiprepGroup.copy(module = Some("fastq_sync"))
  
  addExecutable(Executable("fastqc", Some(""".+""".r)))
  addExecutable(Executable("seqstat", Some(""".+""".r)))
  addExecutable(Executable("seqtkseq", Some(""".+""".r)))
  if (r2.isDefined) addExecutable(Executable("fastqsync", Some(""".+""".r)))
  if (skipTrim != Some(true)) addExecutable(Executable("sickle", Some(""".+""".r)))
  else addNotHavingExecutable("sickle")
  if (skipClip != Some(true)) addNotHavingExecutable("cutadapt")

  override def summaryRoot = summaryLibrary(sampleId, libId)

  def outputFileR1 = new File(outputDir, s"$sampleId-$libId.R1.qc${if (r2.isDefined) ".sync" else ""}.fq.gz")
  def outputFileR2 = r2.map(_ => new File(outputDir, s"$sampleId-$libId.R2.qc.sync.fq.gz"))

  addSummaryFileTest(FileTest(flexiprepGroup, "input_R1", true, true, r1, md5SumInputR1))
  addSummaryFileTest(FileTest(flexiprepGroup, "input_R2", r2.isDefined, true, r2, md5SumInputR2))

  addSummaryFileTest(FileTest(flexiprepGroup, "output_R1", true, keepQcFastqFiles != Some(false), outputFileR1))
  addSummaryFileTest(FileTest(flexiprepGroup, "output_R2", r2.isDefined, keepQcFastqFiles != Some(false), outputFileR2))

  addSettingsTest(flexiprepGroup, "skip_trim" :: Nil, _ shouldBe Some(skipTrim.getOrElse(false)))
  addSettingsTest(flexiprepGroup, "skip_clip" :: Nil, _ shouldBe Some(skipClip.getOrElse(false)))
  addSettingsTest(flexiprepGroup, "paired" :: Nil, _ shouldBe Some(r2.isDefined))

  addStatsTest(fastqcR1Group, shouldExist = true)
  addStatsTest(fastqcR2Group, shouldExist = r2.isDefined)
  addStatsTest(fastqcR1QcGroup, shouldExist = true)
  addStatsTest(fastqcR2QcGroup, shouldExist = r2.isDefined)
  addStatsTest(seqstatR1Group, shouldExist = true)
  addStatsTest(seqstatR2Group, shouldExist = r2.isDefined)
  addStatsTest(seqstatR1QcGroup, shouldExist = true)
  addStatsTest(seqstatR2QcGroup, shouldExist = r2.isDefined)

  addStatsTest(clippingR1Group, shouldExist = r1ContainAdapters && (skipClip != Some(true)))
  addStatsTest(clippingR2Group, shouldExist = r2.isDefined && r2ContainAdapters && (skipClip != Some(true)))
  addStatsTest(trimmingR1Group, shouldExist = skipTrim != Some(true))
  addStatsTest(trimmingR2Group, shouldExist = r2.isDefined && (skipTrim != Some(true)))
  addStatsTest(syncGroup, shouldExist = r2.isDefined)

  addSummaryFileTest(FileTest(fastqcR1Group, "fastqc_data", true, true))
  addSummaryFileTest(FileTest(fastqcR2Group, "fastqc_data", r2.isDefined, true))
  addSummaryFileTest(FileTest(fastqcR1QcGroup, "fastqc_data", true, true))
  addSummaryFileTest(FileTest(fastqcR2QcGroup, "fastqc_data", r2.isDefined, true))

  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testInputR1File() = {
  //    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "input_R1"
  //    validateSummaryFile(summaryFile, file = r1, md5 = Some(md5SumInputR1))
  //    assert(r1.get.exists(), "Input file R1 does not exits anymore")
  //    assert(calcMd5(r1.get) == md5SumInputR1)
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testInputR2File() = {
  //    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "input_R2"
  //    if (r2.isDefined) {
  //      validateSummaryFile(summaryFile, file = r2, md5 = md5SumInputR2)
  //      assert(r2.get.exists(), "Input file R2 does not exits anymore")
  //      md5SumInputR2.foreach(md5 => assert(calcMd5(r2.get) == md5))
  //    } else summaryFile shouldBe JNothing
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testOutputR1File() = {
  //    val outputFile = new File(outputDir, s"$sampleId-$libId.R1.qc${if (r2.isDefined) ".sync" else ""}.fq.gz")
  //    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "output_R1"
  //    validateSummaryFile(summaryFile)
  //    (summaryFile \ "path").extract[String] shouldBe outputFile.getAbsolutePath
  //
  //    if (keepQcFastqFiles != Some(false)) {
  //      assert(outputFile.exists(), "Output file R1 should exist while keepQcFastqFiles=true")
  //
  //      calcMd5(outputFile) shouldBe (summaryFile \ "md5").extract[String]
  //    } else assert(!outputFile.exists(), "Output file R1 should not exist while keepQcFastqFiles=false")
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testOutputR2File() = {
  //    val summaryFile = summaryRoot \ "flexiprep" \ "files" \ "pipeline" \ "output_R2"
  //    if (r2.isDefined) {
  //      val outputFile = new File(outputDir, s"$sampleId-$libId.R2.qc.sync.fq.gz")
  //      validateSummaryFile(summaryFile)
  //      (summaryFile \ "path").extract[String] shouldBe outputFile.getAbsolutePath
  //
  //      if (keepQcFastqFiles == Some(false)) {
  //        assert(outputFile.exists(), "Output file R2 should exist while keepQcFastqFiles=true")
  //
  //        calcMd5(outputFile) shouldBe (summaryFile \ "md5").extract[String]
  //      } else assert(!outputFile.exists(), "Output file R2 should not exist while keepQcFastqFiles=false")
  //    } else {
  //      summaryFile shouldBe JNothing
  //      assert(!outputDir.list().exists(x => x.contains(".R2.") || x.contains(".r2.")))
  //    }
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testSettings(): Unit = {
  //    val settings = summaryRoot \ "flexiprep" \ "settings"
  //    settings shouldBe a[JObject]
  //
  //    settings \ "skip_trim" shouldBe JBool(skipTrim.getOrElse(false))
  //    settings \ "skip_clip" shouldBe JBool(skipClip.getOrElse(false))
  //    settings \ "paired" shouldBe JBool(r2.isDefined)
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"), groups = Array("summaryFastqcR1"))
  //  def testFastqcR1(): Unit = {
  //    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R1"
  //    fastqc shouldBe a[JObject]
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testFastqcR2(): Unit = {
  //    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R2"
  //    if (r2.isDefined) {
  //      fastqc shouldBe a[JObject]
  //      //TODO: check stats
  //    } else fastqc shouldBe JNothing
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testFastqcR1Qc(): Unit = {
  //    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R1_qc"
  //    fastqc shouldBe a[JObject]
  //    //TODO: check stats
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testFastqcR2Qc(): Unit = {
  //    val fastqc = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R2_qc"
  //    if (r2.isDefined) {
  //      fastqc shouldBe a[JObject]
  //      //TODO: check stats
  //    } else fastqc shouldBe JNothing
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testSeqstatR1(): Unit = {
  //    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R1"
  //    seqstat shouldBe a[JObject]
  //    //TODO: check stats
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testSeqstatR2(): Unit = {
  //    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R2"
  //    if (r2.isDefined) {
  //      seqstat shouldBe a[JObject]
  //      //TODO: check stats
  //    } else seqstat shouldBe JNothing
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testSeqstatR1Qc(): Unit = {
  //    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R1_qc"
  //    seqstat shouldBe a[JObject]
  //    //TODO: check stats
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testSeqstatR2Qc(): Unit = {
  //    val seqstat = summaryRoot \ "flexiprep" \ "stats" \ "seqstat_R2_qc"
  //    if (r2.isDefined) {
  //      seqstat shouldBe a[JObject]
  //      //TODO: check stats
  //    } else seqstat shouldBe JNothing
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testClippingR1(): Unit = {
  //    val clipping = summaryRoot \ "flexiprep" \ "stats" \ "clipping_R1"
  //    val adapters = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R1" \ "adapters"
  //    skipClip match {
  //      case Some(false) | None =>
  //        if (adapters.asInstanceOf[JObject].values.nonEmpty) clipping shouldBe a[JObject]
  //        else clipping shouldBe JNothing
  //      //TODO: check stats
  //      case _ => clipping shouldBe JNothing
  //    }
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testClippingR2(): Unit = {
  //    val clipping = summaryRoot \ "flexiprep" \ "stats" \ "clipping_R2"
  //    val adapters = summaryRoot \ "flexiprep" \ "stats" \ "fastqc_R2" \ "adapters"
  //    skipClip match {
  //      case Some(false) | None if r2.isDefined =>
  //        if (adapters.asInstanceOf[JObject].values.nonEmpty) clipping shouldBe a[JObject]
  //        else clipping shouldBe JNothing
  //      //TODO: check stats
  //      case _ => clipping shouldBe JNothing
  //    }
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testTrimmingR1(): Unit = {
  //    val trimming = summaryRoot \ "flexiprep" \ "stats" \ "trimming_R1"
  //    skipTrim match {
  //      case Some(false) | None =>
  //        trimming shouldBe a[JObject]
  //      //TODO: check stats
  //      case _ => trimming shouldBe JNothing
  //    }
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testTrimmingR2(): Unit = {
  //    val trimming = summaryRoot \ "flexiprep" \ "stats" \ "trimming_R2"
  //    skipTrim match {
  //      case Some(false) | None if r2.isDefined =>
  //        trimming shouldBe a[JObject]
  //      //TODO: check stats
  //      case _ => trimming shouldBe JNothing
  //    }
  //  }
  //
  //  @Test(dependsOnGroups = Array("parseSummary"))
  //  def testFastqSync(): Unit = {
  //    val syncing = summaryRoot \ "flexiprep" \ "stats" \ "fastq_sync"
  //    if (r2.isDefined) {
  //      syncing shouldBe a[JObject]
  //      //TODO: check stats
  //    } else syncing shouldBe JNothing
  //  }
  //
}

/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepSingle extends FlexiprepSuccessful {

  /** Input file of this run. */
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))

  override def r1ContainAdapters = true

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe Some(32.244))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe Some(33))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe Some(31))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe Some(34))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe Some(30))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe Some(34))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe Some(21.984))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe Some(30))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe Some(34))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe Some(35))

  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe Some(17.251755265797392))
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe Some(11.735205616850552))
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe Some(52.35707121364093))
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe Some(18.655967903711137))
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe Some(26))
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe Some(21.9))
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe Some(24))
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe Some(28.1))

  addStatsTest(fastqcR1Group, "adapters" :: "TruSeq Adapter, Index 1" :: Nil, _ shouldBe Some("GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG"))
  addStatsTest(fastqcR1Group, "adapters" :: "TruSeq Adapter, Index 18" :: Nil, _ shouldBe Some("GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG"))
  addStatsTest(fastqcR1Group, "adapters" :: Nil, _ shouldBe Some(Map(
    "TruSeq Adapter, Index 1" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG",
    "TruSeq Adapter, Index 18" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG"
  )))

  addStatsTest(seqstatR1Group, "bases" :: "num_total" :: Nil, _ shouldBe Some(100000))
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe Some(21644))
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe Some(23049))
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe Some(25816))
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe Some(26555))
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe Some(2936))
  addStatsTest(seqstatR1Group, "bases" :: "num_qual" :: Nil, x => {
    x should not be empty
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 16497
    array(2) shouldBe 7264
  })

  addStatsTest(seqstatR1Group, "reads" :: "num_total" :: Nil, _ shouldBe Some(1000))
  addStatsTest(seqstatR1Group, "reads" :: "num_with_n" :: Nil, _ shouldBe Some(175))
  addStatsTest(seqstatR1Group, "reads" :: "len_min" :: Nil, _ shouldBe Some(100))
  addStatsTest(seqstatR1Group, "reads" :: "len_max" :: Nil, _ shouldBe Some(100))
  addStatsTest(seqstatR1Group, "reads" :: "qual_encoding" :: Nil, _ shouldBe Some(inputEncodingR1))
  addStatsTest(seqstatR1Group, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x should not be empty
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 1000
    map("60") shouldBe 0
  })
  addStatsTest(seqstatR1Group, "reads" :: "qual_encoding" :: Nil, _ shouldBe Some(inputEncodingR1))

  /** JSON paths for summary. */
  protected val flexiprepPath = Seq("samples", sampleId, "libraries", libId, "flexiprep")
  protected val statsPath = flexiprepPath :+ "stats"
  protected val statsFastqcR1QcPath = statsPath :+ "fastqc_R1_qc"
  protected val statsSeqstatR1QcPath = statsPath :+ "seqstat_R1_qc"

}

/** Trait for Flexiprep runs with paired-end inputs. */
trait FlexiprepPaired extends FlexiprepSingle {

  /** Input read pair 2 for this run. */
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))

  override def r2ContainAdapters = false

  /** MD5 checksum of input read pair 2. */
  override def md5SumInputR2 = Some("1560a4cdc87cc8c4b6701e1253d41f93")

  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe Some(11.351))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe Some(31))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe Some(33))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe Some(5.79))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe Some(2))
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe Some(26))

  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe Some(24.198250728862973))
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe Some(5.247813411078718))
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe Some(48.68804664723032))
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe Some(21.865889212827987))
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe Some(27.769784172661872))
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe Some(19.568345323741006))
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe Some(30.79136690647482))
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe Some(21.8705035971223))

  addStatsTest(fastqcR2Group, "adapters" :: Nil, _ shouldBe Some(Map()))

  addStatsTest(seqstatR2Group, "bases" :: "num_total" :: Nil, _ shouldBe Some(100000))
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe Some(13981))
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe Some(11508))
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe Some(16442))
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe Some(14089))
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe Some(43980))
  addStatsTest(seqstatR2Group, "bases" :: "num_qual" :: Nil, x => {
    x should not be empty
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 2288
    array(2) shouldBe 60383
  })

  addStatsTest(seqstatR2Group, "reads" :: "num_total" :: Nil, _ shouldBe Some(1000))
  addStatsTest(seqstatR2Group, "reads" :: "num_with_n" :: Nil, _ shouldBe Some(769))
  addStatsTest(seqstatR2Group, "reads" :: "len_min" :: Nil, _ shouldBe Some(100))
  addStatsTest(seqstatR2Group, "reads" :: "len_max" :: Nil, _ shouldBe Some(100))
  addStatsTest(seqstatR2Group, "reads" :: "qual_encoding" :: Nil, _ shouldBe Some(inputEncodingR2))
  addStatsTest(seqstatR2Group, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x should not be empty
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 1000
    map("60") shouldBe 0
  })
  addStatsTest(seqstatR2Group, "reads" :: "qual_encoding" :: Nil, _ shouldBe Some(inputEncodingR2))

  /** JSON paths for summary. */
  protected val statsFastqcR2QcPath = statsPath :+ "fastqc_R2_qc"
  protected val statsSeqstatR2QcPath = statsPath :+ "seqstat_R2_qc"

}

