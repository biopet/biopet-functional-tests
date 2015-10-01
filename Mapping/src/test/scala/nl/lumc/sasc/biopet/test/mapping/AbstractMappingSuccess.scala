package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.SummaryPipeline
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 9/17/15.
 */
class AbstractMappingSuccess extends AbstractMapping with SummaryPipeline {
  @Test
  def testFinalBamFile: Unit = {
    val bamFile = new File(outputDir, s"$sampleId-$libId.final.bam")

    bamFile.exists() shouldBe true
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
  }

  @Test
  def testFinalBaiFile: Unit = {
    val baiFile = new File(outputDir, s"$sampleId-$libId.final.bai")

    baiFile.exists() shouldBe true
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test
  def testMarkduplicates: Unit = {
    val bamFile = if (skipMarkDuplicates)
      new File(outputDir, s"$sampleId-$libId.bam")
    else new File(outputDir, s"$sampleId-$libId.dedup.bam")

    val baiFile = if (skipMarkDuplicates)
      new File(outputDir, s"$sampleId-$libId.bai")
    else new File(outputDir, s"$sampleId-$libId.dedup.bai")

    bamFile.exists() shouldBe true
    baiFile.exists() shouldBe true
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
    assert(baiFile.length() > 0, s"$baiFile has size of 0 bytes")
  }

  @Test
  def testSkipFlexiprep: Unit = {
    val flexiprepDir = new File(outputDir, "flexiprep")
    if (skipFlexiprep) {
      assert(!flexiprepDir.exists(), "Flexiprep is skipped but directory exist")
    } else {
      assert(flexiprepDir.exists(), "Flexiprep directory should be there")
      assert(flexiprepDir.isDirectory, s"'$flexiprepDir' should be a directory")
    }
  }

  @Test
  def testSkipMetrics: Unit = {
    val metricsDir = new File(outputDir, "flexiprep")
    if (skipMetrics) {
      assert(!metricsDir.exists(), "Metrics are skipped but directory exist")
    } else {
      assert(metricsDir.exists(), "Metrics directory should be there")
      assert(metricsDir.isDirectory, s"'$metricsDir' should be a directory")
    }
  }
}
