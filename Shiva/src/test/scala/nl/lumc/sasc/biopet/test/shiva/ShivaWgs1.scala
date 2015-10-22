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

class ShivaHaplotypeCallerTest extends ShivaWgs1 {
  override def variantcallers = List("haplotypecaller")
}

class ShivaHaplotypeCallerGvcfTest extends ShivaWgs1 {
  override def variantcallers = List("haplotypecaller_gvcf")
}

class ShivaUnifiedGenotyperTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
}

class ShivaBcftoolsTest extends ShivaWgs1 {
  override def variantcallers = List("bcftools")
}

class ShivaFreebayesTest extends ShivaWgs1 {
  override def variantcallers = List("freebayes")
}

class ShivaRawTest extends ShivaWgs1 {
  override def variantcallers = List("raw")
}

class ShivaSampleLibraryCallingTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def multisampleVariantcalling = Some(false)
  override def libraryVariantcalling = Some(true)
  override def singleSampleVariantcalling = Some(true)
}

class ShivaNoBaserecalTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def useBaseRecalibration = Some(false)
}

class ShivaNoIndelRealignTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
}

class ShivaNoPreprocessTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
  override def useBaseRecalibration = Some(false)
}

