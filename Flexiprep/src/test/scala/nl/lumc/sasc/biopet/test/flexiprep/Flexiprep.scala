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

  def r1: Option[File] = None

  def r2: Option[File] = None

  def args = Seq("-sample", sampleId, "-library", libId, "-cv", "output_dir=" + outputDir, "-run") ++
    r1.collect { case r1 => Seq("-R1", r1.getAbsolutePath) }.getOrElse(Seq()) ++
    r2.collect { case r2 => Seq("-R2", r2.getAbsolutePath) }.getOrElse(Seq())
}

/** Trait representing a successful Flexiprep test group. */
trait SuccessfulFlexiprep extends FlexiprepRun with SummaryPipeline {

  def md5SumInputR1: String
  def md5SumInputR2: Option[String] = None

  @Test
  def testInputFilesChecksum() = {
    assert(calcMd5(r1.get) == md5SumInputR1)
    r2 match {
      case Some(r2File) if md5SumInputR2.isDefined => {
        assert(calcMd5(r2File) == md5SumInputR2.get)
      }
      case _ =>
    }
  }
}

/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepRunSingle extends SuccessfulFlexiprep {

  /** Input file of this run. */
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath))

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"
}

/** Trait for Flexiprep runs with paired-end inputs. */
trait FlexiprepPaired extends FlexiprepRunSingle {

  /** Input read pair 2 for this run. */
  override def r2 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath))

  /** MD5 checksum of input read pair 2. */
  override def md5SumInputR2 = Some("1560a4cdc87cc8c4b6701e1253d41f93")
}
