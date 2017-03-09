package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test._
import nl.lumc.sasc.biopet.utils.ConfigUtils

/** Base trait for Flexiprep pipeline run tests. */
trait FlexiprepRun extends Pipeline {

  def pipelineName = "flexiprep"

  def sampleId = "sampleName"

  def libId = "libName"

  def summaryFile = new File(outputDir, s"$sampleId-$libId.qc.summary.json")

  def r1: Option[File] = None

  def r2: Option[File] = None

  def skipClip: Option[Boolean] = Option(false)

  def skipTrim: Option[Boolean] = Option(false)

  def r1ContainAdapters = false

  def r2ContainAdapters = false

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
  val fastqcR2QcGroup = flexiprepGroup.copy(module = Some("fastqc_R2_qc"))
  val seqstatR2QcGroup = flexiprepGroup.copy(module = Some("seqstat_R2_qc"))

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
  if (skipClip == Some(true) || (!r1ContainAdapters && !r2ContainAdapters)) addNotHavingExecutable("cutadapt")
  else addExecutable(Executable("cutadapt", Some(""".+""".r)))

  override def summaryRoot = summaryLibrary(sampleId, libId)

  def outputFileR1 = new File(outputDir, s"$sampleId-$libId.R1.qc${if (r2.isDefined) ".sync" else ""}.fq.gz")
  def outputFileR2 = r2.map(_ => new File(outputDir, s"$sampleId-$libId.R2.qc.sync.fq.gz"))

  addSummaryFileTest(FileTest(flexiprepGroup, "input_R1", true, true, r1, md5SumInputR1))
  addSummaryFileTest(FileTest(flexiprepGroup, "input_R2", r2.isDefined, true, r2, md5SumInputR2))

  addSummaryFileTest(FileTest(flexiprepGroup, "output_R1", true, keepQcFastqFiles != Some(false), outputFileR1))
  addSummaryFileTest(FileTest(flexiprepGroup, "output_R2", r2.isDefined, keepQcFastqFiles != Some(false), outputFileR2))

  addSettingsTest(flexiprepGroup, "skip_trim" :: Nil, _ shouldBe skipTrim.getOrElse(false))
  addSettingsTest(flexiprepGroup, "skip_clip" :: Nil, _ shouldBe skipClip.getOrElse(false))
  addSettingsTest(flexiprepGroup, "paired" :: Nil, _ shouldBe r2.isDefined)

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
}

/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepSingle extends FlexiprepSuccessful {

  /** Input file of this run. */
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))

  override def r1ContainAdapters = true

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 32.244)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 33)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 30)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 21.984)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 30)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 34)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR1Group, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 35)

  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 17.251755265797392)
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 11.735205616850552)
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 52.35707121364093)
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 18.655967903711137)
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 26)
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 21.9)
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 24)
  addStatsTest(fastqcR1Group, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 28.1)

  addStatsTest(fastqcR1Group, "adapters" :: "TruSeq Adapter, Index 1" :: Nil, _ shouldBe "GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG")
  addStatsTest(fastqcR1Group, "adapters" :: "TruSeq Adapter, Index 18" :: Nil, _ shouldBe "GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG")
  addStatsTest(fastqcR1Group, "adapters" :: Nil, _ shouldBe Map(
    "TruSeq Adapter, Index 18" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACGTCCGCATCTCGTATGCCGTCTTCTGCTTG",
    "TruSeq Adapter, Index 1" -> "GATCGGAAGAGCACACGTCTGAACTCCAGTCACATCACGATCTCGTATGCCGTCTTCTGCTTG",
    "Illumina Universal Adapter" -> "AGATCGGAAGAG"
  ))

  addStatsTest(seqstatR1Group, "bases" :: "num_total" :: Nil, _ shouldBe 100000)
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 21644)
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 23049)
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 25816)
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 26555)
  addStatsTest(seqstatR1Group, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 2936)
  addStatsTest(seqstatR1Group, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 16497
    array(2) shouldBe 7264
  })

  addStatsTest(seqstatR1Group, "reads" :: "num_total" :: Nil, _ shouldBe 1000)
  addStatsTest(seqstatR1Group, "reads" :: "num_with_n" :: Nil, _ shouldBe 175)
  addStatsTest(seqstatR1Group, "reads" :: "len_min" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1Group, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR1Group, "reads" :: "qual_encoding" :: Nil, _ shouldBe inputEncodingR1)
  addStatsTest(seqstatR1Group, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 1000
    map("60") shouldBe 0
  })
}

/** Trait for Flexiprep runs with paired-end inputs. */
trait FlexiprepPaired extends FlexiprepSingle {

  /** Input read pair 2 for this run. */
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))

  override def r2ContainAdapters = true

  /** MD5 checksum of input read pair 2. */
  override def md5SumInputR2 = Some("1560a4cdc87cc8c4b6701e1253d41f93")

  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "mean" :: Nil, _ shouldBe 11.351)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "upper_quartile" :: Nil, _ shouldBe 31)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "1" :: "percentile_90th" :: Nil, _ shouldBe 33)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "mean" :: Nil, _ shouldBe 5.79)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "median" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "lower_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "upper_quartile" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "percentile_10th" :: Nil, _ shouldBe 2)
  addStatsTest(fastqcR2Group, "per_base_sequence_quality" :: "100" :: "percentile_90th" :: Nil, _ shouldBe 26)

  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "A" :: Nil, _ shouldBe 24.198250728862973)
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "T" :: Nil, _ shouldBe 5.247813411078718)
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "G" :: Nil, _ shouldBe 48.68804664723032)
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "1" :: "C" :: Nil, _ shouldBe 21.865889212827987)
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "A" :: Nil, _ shouldBe 27.769784172661872)
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "T" :: Nil, _ shouldBe 19.568345323741006)
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "G" :: Nil, _ shouldBe 30.79136690647482)
  addStatsTest(fastqcR2Group, "per_base_sequence_content" :: "100" :: "C" :: Nil, _ shouldBe 21.8705035971223)

  addStatsTest(fastqcR2Group, "adapters" :: Nil, _ shouldBe Map("Illumina Universal Adapter" -> "AGATCGGAAGAG"))

  addStatsTest(seqstatR2Group, "bases" :: "num_total" :: Nil, _ shouldBe 100000)
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "A" :: Nil, _ shouldBe 13981)
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "T" :: Nil, _ shouldBe 11508)
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "G" :: Nil, _ shouldBe 16442)
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "C" :: Nil, _ shouldBe 14089)
  addStatsTest(seqstatR2Group, "bases" :: "nucleotides" :: "N" :: Nil, _ shouldBe 43980)
  addStatsTest(seqstatR2Group, "bases" :: "num_qual" :: Nil, x => {
    x.isDefined shouldBe true
    val array = ConfigUtils.any2list(x.get).toArray
    array(41) shouldBe 2288
    array(2) shouldBe 60383
  })

  addStatsTest(seqstatR2Group, "reads" :: "num_total" :: Nil, _ shouldBe 1000)
  addStatsTest(seqstatR2Group, "reads" :: "num_with_n" :: Nil, _ shouldBe 769)
  addStatsTest(seqstatR2Group, "reads" :: "len_min" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2Group, "reads" :: "len_max" :: Nil, _ shouldBe 100)
  addStatsTest(seqstatR2Group, "reads" :: "qual_encoding" :: Nil, _ shouldBe inputEncodingR2)
  addStatsTest(seqstatR2Group, "reads" :: "num_avg_qual_gte" :: Nil, x => {
    x.isDefined shouldBe true
    val map = ConfigUtils.any2map(x.get)
    map.size shouldBe 61
    map("0") shouldBe 1000
    map("60") shouldBe 0
  })
}

