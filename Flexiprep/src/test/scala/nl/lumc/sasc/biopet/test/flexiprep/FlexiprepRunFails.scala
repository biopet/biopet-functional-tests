package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, PipelineFail }
import org.testng.annotations.Test

import scala.io.Source

class FlexiprepNoR1ArgTest extends FlexiprepRun with PipelineFail {
  logMustHave("""Argument with name '--inputR1' \(-R1\) is missing""".r)
}

class FlexiprepDryRunNoR1ArgTest extends FlexiprepRun with PipelineFail {
  override def run = false
  logMustHave("""Argument with name '--inputR1' \(-R1\) is missing""".r)
}

class FlexiprepR1NotExistTest extends FlexiprepRun with PipelineFail {
  override def r1 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R1.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r1.get.exists(), s"this file should not exist: $r1")
}

class FlexiprepR2NotExistTest extends FlexiprepRun with PipelineFail {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(new File(Biopet.fixtureDir, "this_should_not_exist.R2.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r2.get.exists(), s"This file should not exist: $r2")
}

class FlexiprepDryRunR1NotExistTest extends FlexiprepRun with PipelineFail {
  override def run = false
  override def r1 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R1.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r1.get.exists(), s"this file should not exist: $r1")
}

class FlexiprepDryRunR2NotExistTest extends FlexiprepRun with PipelineFail {
  override def run = false
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(new File(Biopet.fixtureDir, "this_should_not_exist.R2.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r2.get.exists(), s"This file should not exist: $r2")
}

class FlexiprepNoOutputDirTest extends FlexiprepRun with PipelineFail {
  override def outputDirArg = None
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))

  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: output_dir""".r)
}

class FlexiprepDryRunNoOutputDirTest extends FlexiprepRun with PipelineFail {
  override def outputDirArg = None
  override def run = false
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))

  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: output_dir""".r)
}

class FlexiprepOutOffSyncTest extends FlexiprepRun with PipelineFail {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.not_sync.fq"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.not_sync.fq"))
  logMustHave("""FunctionEdge - Starting""".r)
  logMustHave("""CheckValidateFastq - Corrupt fastq file found, aborting pipeline""".r)
}

class FlexiprepNoEncodingTest extends FlexiprepSuccessful {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.no_encoding.fq"))
  def md5SumInputR1: String = "1d160913e2d0402fc9ef352dd1d4d309"

  @Test
  def testValidateWarning: Unit = {
    val validateLog = new File(outputDir, ".validate_fastq.log.out")
    validateLog should exist
    Source.fromFile(validateLog).getLines().exists(_.contains("- No possible quality encodings found")) shouldBe true
  }
}
