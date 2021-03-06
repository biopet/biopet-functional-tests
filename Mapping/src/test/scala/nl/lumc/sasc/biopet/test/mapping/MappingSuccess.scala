package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import htsjdk.samtools.SamReaderFactory
import nl.lumc.sasc.biopet.test.{Executable, SummaryGroup, SummaryPipeline}
import org.testng.annotations.Test

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * This is a general trait to test a successful run of mapping
  *
  * Created by pjvan_thof on 9/17/15.
  */
trait MappingSuccess extends Mapping with SummaryPipeline {

  val mappingGroup = SummaryGroup("mapping", sample = sampleId, library = libId)
  val bamMetricsGroup = SummaryGroup("bammetrics", sample = sampleId, library = libId)
  val flexiprepGroup = SummaryGroup("flexiprep", sample = sampleId, library = libId)
  val wgsGroup: SummaryGroup = bamMetricsGroup.copy(module = "wgs")
  val bamstatsGroup: SummaryGroup = bamMetricsGroup.copy(module = "bamstats")
  val insertsizeGroup: SummaryGroup = bamMetricsGroup.copy(module = "CollectInsertSizeMetrics")

  def finalBamFile: File =
    if (skipMarkDuplicates.getOrElse(false))
      new File(outputDir, s"${sampleId.get}-${libId.get}.bam")
    else new File(outputDir, s"${sampleId.get}-${libId.get}.dedup.bam")
  def finalWigFile: File = new File(finalBamFile.getAbsolutePath + ".wig")

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

  addSummaryFileTest(mappingGroup, "input_R1", summaryShouldContain = true, true, path = r1)
  addSummaryFileTest(mappingGroup, "input_R2", paired, true, path = r2)

  addSettingsTest(mappingGroup, "skip_metrics" :: Nil, _ shouldBe skipMetrics.getOrElse(false))
  addSettingsTest(mappingGroup, "skip_flexiprep" :: Nil, _ shouldBe skipFlexiprep.getOrElse(false))
  addSettingsTest(mappingGroup,
                  "duplicate_method" :: Nil,
                  _ shouldBe (if (skipMarkDuplicates.getOrElse(false)) "None" else "Picard"))
  addSettingsTest(mappingGroup, "aligner" :: Nil, _ shouldBe aligner.getOrElse("bwa-mem"))

  @Test(dependsOnGroups = Array("summary"))
  def testFinalBamFile(): Unit = {
    testSummaryFiles(
      FileTest(mappingGroup, "output_bam", summaryShouldContain = true, true, path = finalBamFile))
    assert(finalBamFile.length() > 0, s"$finalBamFile has size of 0 bytes")
  }

  @Test
  def testFinalBaiFile(): Unit = {
    val baiFile = new File(finalBamFile.getAbsolutePath.stripSuffix(".bam") + ".bai")
    assert(baiFile.exists())
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test
  def testMarkduplicates(): Unit = {
    val bamFile =
      if (skipMarkDuplicates.contains(true))
        new File(outputDir, s"${sampleId.get}-${libId.get}.bam")
      else new File(outputDir, s"${sampleId.get}-${libId.get}.dedup.bam")

    val baiFile =
      if (skipMarkDuplicates.contains(true))
        new File(outputDir, s"${sampleId.get}-${libId.get}.bai")
      else new File(outputDir, s"${sampleId.get}-${libId.get}.dedup.bai")

    assert(bamFile.exists(), s"Bamfile does not exist: $bamFile")
    assert(baiFile.exists(), s"Bamfile index does not exist: $baiFile")
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test(dependsOnGroups = Array("summary"))
  def testSkipFlexiprep(): Unit = {
    val summaryPipeline =
      Await.result(summaryDb.getPipelines(name = "flexiprep"), Duration.Inf).headOption
    val flexiprepDir = new File(outputDir, "flexiprep")
    if (skipFlexiprep.contains(true)) {
      assert(!flexiprepDir.exists(), "Flexiprep is skipped but directory exist")
      assert(summaryPipeline.isEmpty, "Flexiprep should not be in the summary")
    } else {
      assert(flexiprepDir.exists(), "Flexiprep directory should be there")
      assert(flexiprepDir.isDirectory, s"'$flexiprepDir' should be a directory")
      assert(summaryPipeline.isDefined, "Flexiprep should be in the summary")
    }
  }

  @Test(dependsOnGroups = Array("summary"))
  def testSkipMetrics(): Unit = {
    val summaryPipeline =
      Await.result(summaryDb.getPipelines(name = "bammetrics"), Duration.Inf).headOption
    val metricsDir = new File(outputDir, "metrics")
    if (skipMetrics.contains(true)) {
      assert(!metricsDir.exists(), "Metrics are skipped but directory exist")
      assert(summaryPipeline.isEmpty, "Bammetrics should not be in the summary")
    } else {
      assert(metricsDir.exists(), "Metrics directory should be there")
      assert(metricsDir.isDirectory, s"'$metricsDir' should be a directory")
      assert(summaryPipeline.isDefined, "Bammetrics should be in the summary")
    }
  }

  addSettingsTest(mappingGroup,
                  "chunking" :: Nil,
                  _ shouldBe chunking && !numberChunks.contains(1))
  addSettingsTest(
    mappingGroup,
    "number_of_chunks" :: Nil,
    _ shouldBe (if (chunking && !numberChunks.contains(1)) numberChunks.getOrElse(1) else None))

  @Test
  def testChunkDirs(): Unit = {
    val chunksDir = new File(outputDir, "chunks")
    numberChunks match {
      case Some(n) if chunking && n > 1 =>
        for (i <- 1 to n) {
          val dir = new File(chunksDir, s"$i")
          assert(dir.exists(), s"'$dir' should exist")
          val metrcisDir = new File(dir, "metrics")
          if (chunkMetrics.contains(true))
            assert(metrcisDir.exists(), s"'$metrcisDir' should exist")
          else assert(!metrcisDir.exists(), s"'$metrcisDir' should not exist")
        }
      case _ => assert(!chunksDir.exists())
    }
  }

  @Test
  def testWig(): Unit = {
    val wig = new File(outputDir, finalBamFile.getName + ".wig")
    val tdf = new File(outputDir, finalBamFile.getName + ".tdf")
    val bw = new File(outputDir, finalBamFile.getName + ".bw")
    val generateWig = this.generateWig.getOrElse(false)
    assert(wig.exists() == generateWig)
    assert(tdf.exists() == generateWig)
    assert(bw.exists() == generateWig)
  }

  @Test
  def testReadgroup(): Unit = {
    val inputSam = SamReaderFactory.makeDefault.open(finalBamFile)
    val header = inputSam.getFileHeader
    assert(header.getReadGroups.size() == 1)
    val id = readgroupId.getOrElse(sampleId.get + "-" + libId.get)
    val readgroup = header.getReadGroup(id)
    assert(readgroup != null, s"Readgroup '$id' does not exist in $finalBamFile")

    readgroup.getSample shouldBe sampleId.get
    readgroup.getLibrary match {
      case l if l == readgroupLibrary.orNull =>
      case l if l == libId.get =>
      case null =>
      case l => throw new IllegalStateException(s"Readgroup lbrary is incorrect: '$l'")
    }
    Option(readgroup.getDescription) shouldBe readgroupDescription
    require(
      readgroup.getPlatformUnit == null || readgroup.getPlatformUnit == s"${sampleId.get}-${libId.get}",
      "PU is incorrect")
    Option(readgroup.getPredictedMedianInsertSize) shouldBe predictedInsertsize
    Option(readgroup.getSequencingCenter) shouldBe readgroupSequencingCenter
    readgroup.getPlatform shouldBe platform.getOrElse("illumina")

    inputSam.close()
  }
}
