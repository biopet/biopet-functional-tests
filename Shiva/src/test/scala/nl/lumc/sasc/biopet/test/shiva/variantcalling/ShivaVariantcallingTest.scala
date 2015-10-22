package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvan_thof on 10/22/15.
 */
trait ShivaVariantcallingWgs1 extends ShivaVariantcallingSuccess {
  override def bamFiles = List(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "wgs1.bam"))
}

class HaplotypeCallerTest extends ShivaVariantcallingWgs1 {
  override def variantcallers = List("haplotypecaller")
}

class HaplotypeCallerGvcfTest extends ShivaVariantcallingWgs1 {
  override def variantcallers = List("haplotypecaller_gvcf")
}

class UnifiedGenotyperTest extends ShivaVariantcallingWgs1 {
  override def variantcallers = List("unifiedgenotyper")
}

class BcftoolsTest extends ShivaVariantcallingWgs1 {
  override def variantcallers = List("bcftools")
}

class FreebayesTest extends ShivaVariantcallingWgs1 {
  override def variantcallers = List("freebayes")
}

class RawTest extends ShivaVariantcallingWgs1 {
  override def variantcallers = List("raw")
}
