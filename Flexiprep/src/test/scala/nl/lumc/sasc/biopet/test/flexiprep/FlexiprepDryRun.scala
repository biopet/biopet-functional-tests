package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvanthof on 05/10/15.
 */
class FlexiprepPairedDryRunTest extends FlexiprepRun {
  override def run = false
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath))
  override def r2 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath))
}

class FlexiprepSingleDryRunTest extends FlexiprepRun {
  override def run = false
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath))
}
