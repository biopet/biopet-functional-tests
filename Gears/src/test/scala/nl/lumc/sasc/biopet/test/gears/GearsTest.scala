package nl.lumc.sasc.biopet.test.gears

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvanthof on 27/10/15.
 */
class GearsFastqTest extends GearsSuccessful {
  override def r1 = Some(Biopet.fixtureFile("gears" + File.separator + "hpv_simu_R1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("gears" + File.separator + "hpv_simu_R2.fq.gz"))
}

class GearsBamTest extends GearsSuccessful {
  override def bam = Some(new File(Biopet.fixtureFile("gears" + File.separator + "hpv_simu-testlib.dedup.bam").getAbsolutePath))
}
