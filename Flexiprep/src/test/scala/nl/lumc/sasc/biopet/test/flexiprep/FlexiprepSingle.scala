package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import org.json4s.JValue
import org.testng.annotations.Test

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvan_thof on 10/2/15.
 */
/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepSingle extends FlexiprepSuccessful {

  /** Input file of this run. */
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  if (!skipClip.contains(true)) addExecutable(Executable("cutadapt", Option(""".+""".r)))
}

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

class FlexiprepSingleClipTrimTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")
}

class FlexiprepSingleSkipAllTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
}

class FlexiprepSingleTrimTest extends FlexiprepSingle {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5001a539ca3cc3312835466bdb37b3d8")
}

class FlexiprepSingleDefaultTest extends FlexiprepSingle {
  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")
}

class FlexiprepSingleRemoveOutputTest extends FlexiprepSingleClipTrimTest {
  override def keepQcFastqFiles = Some(false)
}

class FlexiprepSingleKeepOutputTest extends FlexiprepSingleClipTrimTest {
  override def keepQcFastqFiles = Some(true)
}

class FlexiprepSingleUnzippedTest extends FlexiprepSingleClipTrimTest {
  /** Input file of this run. */
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq").getAbsolutePath))

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "b6f564f7496039dfe4e4e9794d191af2"

  if (!skipTrim.contains(true)) addExecutable(Executable("cutadapt", Option(""".+""".r)))
}