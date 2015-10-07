package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.SummaryPipeline
import org.json4s._
import org.testng.annotations.Test

import scala.math._

/**
 * Created by pjvan_thof on 9/17/15.
 */
trait MappingSuccess extends Mapping with SummaryPipeline {

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputFileR1(): Unit = {
    val summaryFile = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "files" \ "input_R1"
    assert(summaryFile.isInstanceOf[JObject])
    assert(r1.get.exists(), "Input file is not there anymore")
    summaryFile shouldBe JString(r1.get.getAbsolutePath)
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputFileR2(): Unit = {
    val summaryFile = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "files" \ "input_R2"
    if (r2.isDefined) {
      assert(summaryFile.isInstanceOf[JObject])
      assert(r2.get.exists(), "Input file is not there anymore")
      summaryFile shouldBe JString(r2.get.getAbsolutePath)
    } else summaryFile shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testSettings: Unit = {
    val settings = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "settings"
    assert(settings.isInstanceOf[JObject])

    settings \ "skip_metrics" shouldBe JBool(skipMetrics.getOrElse(false))
    settings \ "skip_flexiprep" shouldBe JBool(skipFlexiprep.getOrElse(false))
    settings \ "skip_markduplicates" shouldBe JBool(skipMarkDuplicates.getOrElse(false))
    settings \ "aligner" shouldBe JString(aligner.getOrElse("bwamem"))
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testFinalBamFile(): Unit = {
    val bamFile = new File(outputDir, s"$sampleId-$libId.final.bam")
    val summaryFile = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "files" \ "output_bamfile"
    assert(summaryFile.isInstanceOf[JObject])
    summaryFile \ "path" shouldBe JString(bamFile.getAbsolutePath)

    assert(bamFile.exists())
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
  }

  @Test
  def testFinalBaiFile(): Unit = {
    val baiFile = new File(outputDir, s"$sampleId-$libId.final.bai")

    baiFile.exists() shouldBe true
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test
  def testMarkduplicates(): Unit = {
    val bamFile = if (skipMarkDuplicates.contains(true))
      new File(outputDir, s"$sampleId-$libId.bam")
    else new File(outputDir, s"$sampleId-$libId.dedup.bam")

    val baiFile = if (skipMarkDuplicates.contains(true))
      new File(outputDir, s"$sampleId-$libId.bai")
    else new File(outputDir, s"$sampleId-$libId.dedup.bai")

    bamFile.exists() shouldBe true
    baiFile.exists() shouldBe true
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
  def testChunkNumber: Unit = {
    def calcNumberChunk: Int = {
      val fileSize = r1.get.length()
      val size = if (r1.get.getName.endsWith(".gz") || r1.get.getName.endsWith(".gzip")) r1.get.length * 3 else r1.get.length
      ceil(fileSize.toDouble / size).toInt
    }

    val settings = summary \ "samples" \ sampleId.get \ "libraries" \ libId.get \ "mapping" \ "settings"
    settings \ "chunking" shouldBe JBool(chunking.getOrElse(chunksize.exists(_ > 1)))
    if (chunking.getOrElse(chunksize.exists(_ > 1))) {
      settings \ "numberChunks" shouldBe JInt(BigInt(numberChunks.getOrElse(calcNumberChunk)))
    } else settings \ "numberChunks" shouldBe JInt(BigInt(numberChunks.getOrElse(1)))
  }
}
