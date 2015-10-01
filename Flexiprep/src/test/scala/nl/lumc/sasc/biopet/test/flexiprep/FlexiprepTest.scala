package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import org.testng.annotations.Test

import nl.lumc.sasc.biopet.test.{ Biopet, PipelineFail, SummaryPipeline }

/** Trait representing a successful Flexiprep test group. */
trait SuccessfulFlexiprepTest extends AbstractFlexiprep with SummaryPipeline

/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepRunSingle extends SuccessfulFlexiprepTest {

  /** Base command-line arguments for this run. */
  abstract override def args = super.args ++ Seq("-R1", inputR1.toString)

  /** Input file of this run. */
  lazy val inputR1 = new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath)

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  @Test(priority = -2) def testInputFilesChecksum() = assert(calcMd5(inputR1) == md5SumInputR1)
}

/** Trait for Flexiprep runs with paired-end inputs. */
trait FlexiprepPairedTest extends SuccessfulFlexiprepTest {

  /** Base command-line arguments for this run. */
  abstract override def args = super.args ++ Seq("-R1", inputR1.toString, "-R2", inputR2.toString)

  /** Input read pair 1 for this run. */
  lazy val inputR1 = new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath)

  /** MD5 checksum of input read pair 1. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  /** Input read pair 2 for this run. */
  lazy val inputR2 = new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath)

  /** MD5 checksum of input read pair 2. */
  def md5SumInputR2 = "1560a4cdc87cc8c4b6701e1253d41f93"
}

class FlexiprepRunSingleClipTrimTest extends FlexiprepRunSingle {

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "5b7896e489a5aeb3d30cb11ea15a7be3")
  }
}

class FlexiprepRunSingleClipTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_trim=true")

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "037aa58f60372c11037bef9ac157777e")
  }
}

class FlexiprepRunSingleTrimTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=true")

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "5001a539ca3cc3312835466bdb37b3d8")
  }
}

class FlexiprepRunSingleTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=true")

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(!outputR1.exists)
  }
}

class FlexiprepNoR1Test extends AbstractFlexiprep with PipelineFail {
  override def args = super.args ++ Seq("-run")
}