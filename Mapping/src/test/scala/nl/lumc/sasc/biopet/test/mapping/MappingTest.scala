package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.flexiprep.AbstractFlexiprep
import nl.lumc.sasc.biopet.test.{PipelineFail, TestReference, Biopet}
import org.scalatest.Matchers
import org.testng.annotations.BeforeClass

/**
 * Created by pjvan_thof on 7/6/15.
 */
class MappingSingleTest extends AbstractMappingSuccess with TestReference {
  override def args = super.args ++ Seq("-run")

  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
}

class MappingPairedTest extends AbstractMappingSuccess with TestReference {
  override def args = super.args ++ Seq("-run")

  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

class MappingSkipTest extends AbstractMappingSuccess with TestReference {
  override def args = super.args ++ Seq("-run")

  override def skipFlexiprep = true
  override def skipMetrics = true
  override def skipMarkDuplicates = true

  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

class MappingNoR1Test extends AbstractMapping with PipelineFail {
  override def args = super.args ++ Seq("-run")
}