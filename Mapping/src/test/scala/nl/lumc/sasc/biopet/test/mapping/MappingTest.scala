package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import org.scalatest.Matchers

/**
 * Created by pjvan_thof on 7/6/15.
 */
class MappingSingleTest extends AbstractMapping {
  override def args = super.args ++ Seq("-run")
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
}

class MappingPairedTest extends AbstractMapping {
  override def args = super.args ++ Seq("-run")
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}