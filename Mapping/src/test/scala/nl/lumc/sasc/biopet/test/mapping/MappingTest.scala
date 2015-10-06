package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.{ PipelineFail, TestReference, Biopet }

/**
 * Created by pjvan_thof on 7/6/15.
 */
class MappingSingleTest extends AbstractMappingSuccess with TestReference {
  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
}

class MappingPairedTest extends AbstractMappingSuccess with TestReference {
  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))
}

class MappingSkipTest extends AbstractMappingSuccess with TestReference {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)

  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))
}

class MappingNoR1Test extends AbstractMapping with PipelineFail