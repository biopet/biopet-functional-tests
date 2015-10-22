package nl.lumc.sasc.biopet.test.gears

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by wyleung on 22-10-15.
 */
class GearsFastQDryRunTest extends GearsRun {
  override def run = false

  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))

  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

class GearsBamDryRunTest extends GearsRun {
  override def run = false

  override def r1 = Some(new File(Biopet.fixtureFile("gears" + File.separator + "hpv_simu.bam").getAbsolutePath))
}
