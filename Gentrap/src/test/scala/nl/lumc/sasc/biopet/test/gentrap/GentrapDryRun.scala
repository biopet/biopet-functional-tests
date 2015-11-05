package nl.lumc.sasc.biopet.test.gentrap

import nl.lumc.sasc.biopet.test.Samples

/**
 * Created by pjvanthof on 05/11/15.
 */
class GentrapDryRunTest extends Gentrap {
  override def configs = super.configs ::: Samples.wgs1Config :: Nil
}
