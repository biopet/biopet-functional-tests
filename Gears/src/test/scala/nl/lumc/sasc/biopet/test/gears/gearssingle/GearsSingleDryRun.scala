package nl.lumc.sasc.biopet.test.gears.gearssingle

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
  * Created by wyleung on 22-10-15.
  */
class GearsSingleFastqDryRunTest extends GearsSingle {
  override def run = false

  override def r1 = Some(Biopet.fixtureFile("gears" + File.separator + "hpv_simu_R1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("gears" + File.separator + "hpv_simu_R2.fq.gz"))
}

class GearsSingleBamDryRunTest extends GearsSingle {
  override def run = false

  override def bam =
    Some(new File(
      Biopet.fixtureFile("gears" + File.separator + "hpv_simu-testlib.dedup.bam").getAbsolutePath))
}
