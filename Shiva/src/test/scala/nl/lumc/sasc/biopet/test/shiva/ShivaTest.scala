package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.{ TestReference, Samples }

/**
 * Created by pjvan_thof on 10/1/15.
 */
class ShivaTest extends AbstractShivaSuccess with TestReference {
  def sampleConfigs = Samples.wgs1Config :: Nil
}