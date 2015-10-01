package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File
import nl.lumc.sasc.biopet.test.{ Biopet, PipelineFail, SummaryPipeline }
import org.scalatest.Matchers

abstract class AbstractFlexiprepSuccess extends AbstractFlexiprep with SummaryPipeline

class FlexiprepSingleTest extends AbstractFlexiprepSuccess {
  override def args = super.args ++ Seq(
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath
  )
}

class FlexiprepPairedTest extends AbstractFlexiprepSuccess {
  override def args = super.args ++ Seq(
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath,
    "-R2", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath
  )
}

class FlexiprepNoR1Test extends AbstractFlexiprep with PipelineFail {
  override def args = super.args ++ Seq("-run")
}