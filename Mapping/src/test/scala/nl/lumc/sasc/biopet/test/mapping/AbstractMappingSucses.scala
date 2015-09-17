package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 9/17/15.
 */
class AbstractMappingSuccess extends AbstractMapping {
  @Test
  def testFinalBamFile: Unit = {
    val bamFile = new File(outputDir, s"$sampleId-$libId.final.bam")
    val baiFile = new File(outputDir, s"$sampleId-$libId.final.bai")

    bamFile.exists() shouldBe true
    baiFile.exists() shouldBe true
    assert(bamFile.length() > 0, s"$bamFile has size of 0 bytes")
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
}
