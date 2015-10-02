package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, SummaryPipeline, Pipeline }
import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

/** Base trait for Flexiprep pipeline run tests. */
trait FlexiprepRun extends Pipeline {

  def pipelineName = "flexiprep"

  def sampleId = "sampleName"

  def libId = "libName"

  def summaryFile = new File(outputDir, s"$sampleId-$libId.qc.summary.json")

  def args = Seq("-sample", sampleId, "-library", libId, "-cv", "output_dir=" + outputDir, "-run")
}

/** Trait representing a successful Flexiprep test group. */
trait SuccessfulFlexiprepTest extends FlexiprepRun with SummaryPipeline

/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepRunSingle extends SuccessfulFlexiprepTest {

  /** Base command-line arguments for this run. */
  abstract override def args = super.args ++ Seq("-R1", inputR1.getAbsolutePath)

  /** Input file of this run. */
  val inputR1 = new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath)

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  @Test
  def testInputFilesChecksum() = {
    assert(calcMd5(inputR1) == md5SumInputR1)
  }
}

/** Trait for Flexiprep runs with paired-end inputs. */
trait FlexiprepPairedTest extends SuccessfulFlexiprepTest {

  /** Base command-line arguments for this run. */
  abstract override def args = super.args ++ Seq("-R1", inputR1.getAbsolutePath, "-R2", inputR2.getAbsolutePath)

  /** Input read pair 1 for this run. */
  val inputR1 = new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath)

  /** MD5 checksum of input read pair 1. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"

  /** Input read pair 2 for this run. */
  val inputR2 = new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath)

  /** MD5 checksum of input read pair 2. */
  def md5SumInputR2 = "1560a4cdc87cc8c4b6701e1253d41f93"

  @Test
  def testInputFilesChecksum() = {
    assert(calcMd5(inputR1) == md5SumInputR1)
    assert(calcMd5(inputR2) == md5SumInputR2)
  }
}
