package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, PipelineFail }
import org.testng.annotations.Test

class FlexiprepNoR1ArgTest extends FlexiprepRun with PipelineFail {
  override def args = super.args ++ Seq("-run")
  logMustHave("""Argument with name '--input_r1' (-R1) is missing""".r)
}

class FlexiprepR1NotExistTest extends FlexiprepRun with PipelineFail {
  override def r1 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R1.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist = assert(!r1.get.exists(), s"this file should not exist: $r1")
}

class FlexiprepR2NotExistTest extends FlexiprepRun with PipelineFail {
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath))
  override def r2 = Some(new File(Biopet.fixtureDir, "this_should_not_exist.R2.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist = assert(!r2.get.exists(), s"This file should not exist: $r2")
}