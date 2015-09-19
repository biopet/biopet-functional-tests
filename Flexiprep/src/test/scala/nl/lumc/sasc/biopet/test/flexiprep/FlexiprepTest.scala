package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.{ PipelineFail, Biopet }
import org.scalatest.Matchers

/**
 * Created by pjvan_thof on 7/6/15.
 */
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

class FlexiprepNoR1Test extends AbstractFlexiprep with PipelineFail