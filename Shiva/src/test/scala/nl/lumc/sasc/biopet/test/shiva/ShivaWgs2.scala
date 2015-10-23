package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.{ Samples, TestReference }

/**
 * Created by pjvan_thof on 10/23/15.
 */
trait ShivaWgs2 extends ShivaSuccess with TestReference {
  override def configs = super.configs ::: Samples.wgs2Config :: Nil

  def samples = Map("wgs2" -> List("lib1", "lib2"))
}

class Wgs2HaplotypeCallerTest extends ShivaWgs2 {
  override def variantcallers = List("haplotypecaller")
}

class Wgs2HaplotypeCallerGvcfTest extends ShivaWgs2 {
  override def variantcallers = List("haplotypecaller_gvcf")
}

class Wgs2UnifiedGenotyperTest extends ShivaWgs2 {
  override def variantcallers = List("unifiedgenotyper")
}

class Wgs2BcftoolsTest extends ShivaWgs2 {
  override def variantcallers = List("bcftools")
}

class Wgs2BcftoolsSinglesampleTest extends ShivaWgs2 {
  override def variantcallers = List("bcftools_singlesample")
}

class Wgs2FreebayesTest extends ShivaWgs2 {
  override def variantcallers = List("freebayes")
}

class Wgs2RawTest extends ShivaWgs2 {
  override def variantcallers = List("raw")
}

class Wgs2SampleLibraryCallingTest extends ShivaWgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def multisampleVariantcalling = Some(false)
  override def libraryVariantcalling = Some(true)
  override def singleSampleVariantcalling = Some(true)
}

class Wgs2NoBaserecalTest extends ShivaWgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def useBaseRecalibration = Some(false)
}

class Wgs2NoIndelRealignTest extends ShivaWgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
}

class Wgs2NoPreprocessTest extends ShivaWgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
  override def useBaseRecalibration = Some(false)
}

