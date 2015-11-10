package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import htsjdk.samtools.SamReaderFactory
import nl.lumc.sasc.biopet.test.SummaryPipeline
import org.json4s._
import org.testng.annotations.Test

import scala.math._

/**
 * This is a general trait to test a successful run of mapping
 *
 * Created by pjvan_thof on 9/17/15.
 */
trait MappingSuccess extends Mapping with SummaryPipeline {

  def summaryFile = new File(outputDir, s"${sampleId.get}-${libId.get}.summary.json")

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputFileR1(): Unit = {
    val summaryFile = summaryRoot \ "mapping" \ "files" \ "pipeline" \ "input_R1"
    validateSummaryFile(summaryFile, r1)
    assert(r1.get.exists(), "Input file is not there anymore")
  }

  logMustNotHave("""Script failed with \d+ total jobs""".r)
  logMustHave("""Script completed successfully with \d+ total jobs""".r)

  if (!skipFlexiprep.contains(true)) {
    addExecutable(Executable("fastqc", Some(""".+""".r)))
    addExecutable(Executable("seqstat", Some(""".+""".r)))
    addExecutable(Executable("seqtkseq", Some(""".+""".r)))
    if (paired) addExecutable(Executable("fastqsync", Some(""".+""".r)))
    else addNotHavingExecutable("fastqsync")
  } else {
    addNotHavingExecutable("fastqc")
    addNotHavingExecutable("seqtkseq")
    addNotHavingExecutable("seqstat")
    addNotHavingExecutable("sickle")
    addNotHavingExecutable("cutadapt")
    addNotHavingExecutable("fastqsync")
  }

  if (aligner.isEmpty || aligner.contains("bwa-mem")) {
    addExecutable(Executable("bwamem", Some(""".+""".r)))
    addExecutable(Executable("sortsam", Some(""".+""".r)))
  } else addNotHavingExecutable("bwamem")

  if (aligner.contains("bowtie")) {
    addExecutable(Executable("bowtie", Some(""".+""".r)))
    addExecutable(Executable("addorreplacereadgroups", Some(""".+""".r)))
  } else addNotHavingExecutable("bowtie")

  if (aligner.contains("gsnap")) {
    addExecutable(Executable("gsnap", Some(""".+""".r)))
    addExecutable(Executable("reordersam", Some(""".+""".r)))
    addExecutable(Executable("addorreplacereadgroups", Some(""".+""".r)))
  } else addNotHavingExecutable("gsnap")

  if (aligner.contains("star") || aligner.contains("star-2pass")) {
    addExecutable(Executable("star", Some(""".+""".r)))
    addExecutable(Executable("addorreplacereadgroups", Some(""".+""".r)))
  } else addNotHavingExecutable("star")

  if (aligner.contains("tophat")) {
    addExecutable(Executable("tophat", Some(""".+""".r)))
    addExecutable(Executable("reordersam", Some(""".+""".r)))
    addExecutable(Executable("addorreplacereadgroups", Some(""".+""".r)))
  } else addNotHavingExecutable("tophat")

  if (skipMarkDuplicates.contains(true)) addNotHavingExecutable("markduplicates")
  else addExecutable(Executable("markduplicates", Some(""".+""".r)))

  override def summaryRoot = summaryLibrary(sampleId.get, libId.get)

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputFileR2(): Unit = {
    val summaryFile = summaryRoot \ "mapping" \ "files" \ "pipeline" \ "input_R2"
    if (paired) {
      validateSummaryFile(summaryFile, r2)
      assert(r2.get.exists(), "Input file is not there anymore")
    } else summaryFile shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSettings(): Unit = {
    val settings = summaryRoot \ "mapping" \ "settings"
    settings shouldBe a[JObject]

    settings \ "skip_metrics" shouldBe JBool(skipMetrics.getOrElse(false))
    settings \ "skip_flexiprep" shouldBe JBool(skipFlexiprep.getOrElse(false))
    settings \ "skip_markduplicates" shouldBe JBool(skipMarkDuplicates.getOrElse(false))
    settings \ "aligner" shouldBe JString(aligner.getOrElse("bwa-mem"))
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFinalBamFile(): Unit = {
    val summaryFile = summaryRoot \ "mapping" \ "files" \ "pipeline" \ "output_bamfile"
    validateSummaryFile(summaryFile, Some(finalBamFile))

    assert(finalBamFile.exists())
    assert(finalBamFile.length() > 0, s"$finalBamFile has size of 0 bytes")
  }

  def finalBamFile: File = new File(outputDir, s"${sampleId.get}-${libId.get}.final.bam")
  def finalWigFile: File = new File(outputDir, s"${sampleId.get}-${libId.get}.final.bam.wig")

  @Test
  def testFinalBaiFile(): Unit = {
    val baiFile = new File(outputDir, s"${sampleId.get}-${libId.get}.final.bai")

    assert(baiFile.exists())
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test
  def testMarkduplicates(): Unit = {
    val bamFile = if (skipMarkDuplicates.contains(true))
      new File(outputDir, s"${sampleId.get}-${libId.get}.bam")
    else new File(outputDir, s"${sampleId.get}-${libId.get}.dedup.bam")

    val baiFile = if (skipMarkDuplicates.contains(true))
      new File(outputDir, s"${sampleId.get}-${libId.get}.bai")
    else new File(outputDir, s"${sampleId.get}-${libId.get}.dedup.bai")

    assert(bamFile.exists(), s"Bamfile does not exist: $bamFile")
    assert(baiFile.exists(), s"Bamfile index does not exist: $baiFile")
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSkipFlexiprep(): Unit = {
    val flexiprepSummary = summaryRoot \ "flexiprep"
    val flexiprepDir = new File(outputDir, "flexiprep")
    if (skipFlexiprep.contains(true)) {
      assert(!flexiprepDir.exists(), "Flexiprep is skipped but directory exist")
      flexiprepSummary shouldBe JNothing
    } else {
      flexiprepSummary shouldBe a[JObject]
      assert(flexiprepDir.exists(), "Flexiprep directory should be there")
      assert(flexiprepDir.isDirectory, s"'$flexiprepDir' should be a directory")
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSkipMetrics(): Unit = {
    val metricsSummary = summaryRoot \ "bammetrics"
    val metricsDir = new File(outputDir, "metrics")
    if (skipMetrics.contains(true)) {
      assert(!metricsDir.exists(), "Metrics are skipped but directory exist")
      metricsSummary shouldBe JNothing
    } else {
      metricsSummary shouldBe a[JObject]
      assert(metricsDir.exists(), "Metrics directory should be there")
      assert(metricsDir.isDirectory, s"'$metricsDir' should be a directory")
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testChunkNumber(): Unit = {
    val settings = summaryRoot \ "mapping" \ "settings"
    settings \ "chunking" shouldBe JBool(chunking)
    if (chunking) settings \ "numberChunks" shouldBe JInt(BigInt(numberChunks.getOrElse(1)))
    else settings \ "numberChunks" shouldBe JNull
  }

  @Test
  def testChunkDirs(): Unit = {
    val chunksDir = new File(outputDir, "chunks")
    numberChunks match {
      case Some(n) if chunking =>
        for (i <- 1 to n) {
          val dir = new File(chunksDir, s"$i")
          assert(dir.exists(), s"'$dir' should exist")
          val metrcisDir = new File(dir, "metrics")
          if (chunkMetrics.contains(true)) assert(metrcisDir.exists(), s"'$metrcisDir' should exist")
          else assert(!metrcisDir.exists(), s"'$metrcisDir' should not exist")
        }
      case _ => assert(!chunksDir.exists())
    }
  }

  @Test
  def testWig(): Unit = {
    val bamFile = new File(outputDir, s"${sampleId.get}-${libId.get}.final.bam")
    val wig = new File(outputDir, bamFile.getName + ".wig")
    val tdf = new File(outputDir, bamFile.getName + ".tdf")
    val bw = new File(outputDir, bamFile.getName + ".bw")
    val generateWig = this.generateWig.getOrElse(false)
    assert(wig.exists() == generateWig)
    assert(tdf.exists() == generateWig)
    assert(bw.exists() == generateWig)
  }

  @Test
  def testReadgroup(): Unit = {
    val bamFile = new File(outputDir, s"${sampleId.get}-${libId.get}.final.bam")
    val inputSam = SamReaderFactory.makeDefault.open(bamFile)
    val header = inputSam.getFileHeader
    assert(header.getReadGroups.size() == 1)
    val id = readgroupId.getOrElse(sampleId.get + "-" + libId.get)
    val readgroup = header.getReadGroup(id)
    assert(readgroup != null, s"Readgroup '$id' does not exist in $bamFile")

    readgroup.getSample shouldBe sampleId.get
    readgroup.getLibrary shouldBe libId.get
    Option(readgroup.getDescription) shouldBe readgroupDescription
    readgroup.getPlatformUnit shouldBe platformUnit.getOrElse("na")
    Option(readgroup.getPredictedMedianInsertSize) shouldBe predictedInsertsize
    Option(readgroup.getSequencingCenter) shouldBe readgroupSequencingCenter
    readgroup.getPlatform shouldBe platform.getOrElse("illumina")

    inputSam.close()
  }
}
