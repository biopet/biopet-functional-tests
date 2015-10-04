package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, SummaryPipeline, Pipeline }
import org.json4s._
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

  /** This is the uncompressed md5sum of the output R1 */
  def md5SumOutputR1: Option[String] = None

  /** This is the uncompressed md5sum of the output R2 */
  def md5SumOutputR2: Option[String] = None

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputR1File = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "input_R1"
    validateSummaryFile(summaryFile)
    assert(calcMd5(r1.get) == md5SumInputR1)
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testInputR2File = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "input_R2"
    if (r2.isDefined) {
      validateSummaryFile(summaryFile, md5 = md5SumInputR2)
      md5SumInputR2.foreach(md5 => assert(calcMd5(r2.get) == md5))
    } else summaryFile shouldBe JNothing
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR1File = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "output_R1"
    validateSummaryFile(summaryFile)
    val file = new File((summaryFile \ "path").extract[String])

    md5SumOutputR1.foreach(calcMd5Unzipped(file) shouldBe _)
    calcMd5(file) shouldBe (summaryFile \ "md5").extract[String]
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR2File = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "pipeline" \ "output_R2"
    if (r2.isDefined) {
      validateSummaryFile(summaryFile)
      val file = new File((summaryFile \ "path").extract[String])

      md5SumOutputR2.foreach(calcMd5Unzipped(file) shouldBe _)
      calcMd5(file) shouldBe (summaryFile \ "md5").extract[String]
    } else {
      summaryFile shouldBe JNothing
    }
  }

}
