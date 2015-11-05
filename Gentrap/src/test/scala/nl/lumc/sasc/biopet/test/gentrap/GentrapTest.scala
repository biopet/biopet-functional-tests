package nl.lumc.sasc.biopet.test.gentrap

import nl.lumc.sasc.biopet.test.Samples

/**
 * Created by pjvanthof on 05/11/15.
 */
class GentrapTest extends GentrapSuccess {
  override def configs = super.configs ::: Samples.wgs1Config :: Nil

  def samples = Map("wgs1" -> List("lib1"))
}
