package nl.lumc.sasc.biopet.test.flexiprep

import org.json4s.JValue
import org.testng.annotations.Test

class FlexiprepSingleClipTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")

  lazy val fastqcR1: JValue = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "stats" \ "fastqc_R1"
  lazy val pbsR1: JValue = fastqcR1 \ "per_base_sequence_quality"

  @Test(dependsOnGroups = Array("summaryFastqcR1"))
  def testPerBaseSequenceQualityR1Size() = pbsR1.children.size shouldBe 55

  @Test(dependsOnGroups = Array("summaryFastqcR1"))
  def testPerBaseSequenceQualityR1First() = {
    pbsR1 \ "1" \ "mean" shouldBe 32.23529411764706
    pbsR1 \ "1" \ "median" shouldBe 33.0
    pbsR1 \ "1" \ "lower_quartile" shouldBe 31.0
    pbsR1 \ "1" \ "upper_quartile" shouldBe 34.0
    pbsR1 \ "1" \ "percentile_10th" shouldBe 30.0
    pbsR1 \ "1" \ "percentile_90th" shouldBe 34.0
  }

  @Test(dependsOnGroups = Array("summaryFastqcR1"))
  def testPerBaseSequenceQualityR1Last() = {
    pbsR1 \ "100" \ "mean" shouldBe 21.59223300970874
    pbsR1 \ "100" \ "median" shouldBe 29.0
    pbsR1 \ "100" \ "lower_quartile" shouldBe 2.0
    pbsR1 \ "100" \ "upper_quartile" shouldBe 34.0
    pbsR1 \ "100" \ "percentile_10th" shouldBe 2.0
    pbsR1 \ "100" \ "percentile_90th" shouldBe 35.0
  }
}
