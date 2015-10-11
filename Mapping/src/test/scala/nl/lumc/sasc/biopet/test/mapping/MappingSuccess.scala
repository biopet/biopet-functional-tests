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

  logMustNotHave("""Script failed with \d+ total jobs""".r)
  logMustHave("""Script completed successfully with \d+ total jobs""".r)

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputFileR1(): Unit = {
    val summaryFile = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "files" \ "pipeline" \ "input_R1"
    assert(summaryFile.isInstanceOf[JObject])
    assert(r1.get.exists(), "Input file is not there anymore")
    summaryFile \ "path" shouldBe JString(r1.get.getAbsolutePath)
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputFileR2(): Unit = {
    val summaryFile = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "files" \ "pipeline" \ "input_R2"
    if (r2.isDefined) {
      assert(summaryFile.isInstanceOf[JObject])
      assert(r2.get.exists(), "Input file is not there anymore")
      summaryFile \ "path" shouldBe JString(r2.get.getAbsolutePath)
    } else summaryFile shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSettings(): Unit = {
    val settings = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "settings"
    assert(settings.isInstanceOf[JObject])

    settings \ "skip_metrics" shouldBe JBool(skipMetrics.getOrElse(false))
    settings \ "skip_flexiprep" shouldBe JBool(skipFlexiprep.getOrElse(false))
    settings \ "skip_markduplicates" shouldBe JBool(skipMarkDuplicates.getOrElse(false))
    settings \ "aligner" shouldBe JString(aligner.getOrElse("bwa-mem"))
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFinalBamFile(): Unit = {
    val bamFile = new File(outputDir, s"${sampleId.get}-${libId.get}.final.bam")
    val summaryFile = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "files" \ "pipeline" \ "output_bamfile"
    assert(summaryFile.isInstanceOf[JObject])
    summaryFile \ "path" shouldBe JString(bamFile.getAbsolutePath)

    assert(bamFile.exists())
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
  }

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
    assert(baiFile.exists(), s"Bamfile idnex does not exist: $baiFile")
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSkipFlexiprep(): Unit = {
    val flexiprepSummary = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "flexiprep"
    val flexiprepDir = new File(outputDir, "flexiprep")
    if (skipFlexiprep.contains(true)) {
      assert(!flexiprepDir.exists(), "Flexiprep is skipped but directory exist")
      flexiprepSummary shouldBe JNothing
    } else {
      assert(flexiprepSummary.isInstanceOf[JObject])
      assert(flexiprepDir.exists(), "Flexiprep directory should be there")
      assert(flexiprepDir.isDirectory, s"'$flexiprepDir' should be a directory")
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSkipMetrics(): Unit = {
    val metricsSummary = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "bammetrics"
    val metricsDir = new File(outputDir, "metrics")
    if (skipMetrics.contains(true)) {
      assert(!metricsDir.exists(), "Metrics are skipped but directory exist")
      metricsSummary shouldBe JNothing
    } else {
      assert(metricsSummary.isInstanceOf[JObject])
      assert(metricsDir.exists(), "Metrics directory should be there")
      assert(metricsDir.isDirectory, s"'$metricsDir' should be a directory")
    }
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testChunkNumber(): Unit = {
    val settings = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "settings"
    settings \ "chunking" shouldBe JBool(chunking)
    settings \ "numberChunks" shouldBe JInt(BigInt(numberChunks.getOrElse(1)))
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
