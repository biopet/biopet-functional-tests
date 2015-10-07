package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.{TestReference, Biopet}

/**
 * Created by pjvan_thof on 10/7/15.
 */
trait MappingSingle extends MappingSuccess with TestReference {
  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
}

class MappingSingleTest extends MappingSingle

class MappingSingleSkipTest extends MappingSingle {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)
}
