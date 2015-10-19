package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.{ TestReference, Samples }
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 10/1/15.
 */
class ShivaTest extends ShivaSuccess with TestReference {
  val configFile = createTempConfig(Map("variantcallers" -> List("haplotypecaller", "haplotypecaller_gvcf")))
  override def configs = super.configs ::: Samples.wgs1Config :: configFile :: Nil
}