package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.{ TestReference, Samples }
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 10/1/15.
 */
trait ShivaWgs1 extends ShivaSuccess with TestReference {
  override def configs = super.configs ::: Samples.wgs1Config :: Nil

  def samples = Map("wgs1" -> List("lib1"))
}

class ShivaHaplotypeCaller extends ShivaWgs1 {
  override def variantcallers = List("haplotypecaller")
}

class ShivaHaplotypeCallerGvcf extends ShivaWgs1 {
  override def variantcallers = List("haplotypecaller_gvcf")
}

class ShivaUnifiedGenotyper extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
}

class ShivaBcftools extends ShivaWgs1 {
  override def variantcallers = List("bcftools")
}

class ShivaFreebayes extends ShivaWgs1 {
  override def variantcallers = List("freebayes")
}

class ShivaRaw extends ShivaWgs1 {
  override def variantcallers = List("raw")
}