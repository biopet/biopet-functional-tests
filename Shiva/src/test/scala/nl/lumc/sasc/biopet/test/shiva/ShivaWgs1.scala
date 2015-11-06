package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, TestReference, Samples }
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 10/1/15.
 */
trait ShivaWgs1 extends ShivaSuccess with TestReference {
  override def configs = super.configs ::: Samples.wgs1Config :: Nil

  def samples = Map("wgs1" -> List("lib1"))
}

class Wgs1HaplotypeCallerTest extends ShivaWgs1 {
  override def variantcallers = List("haplotypecaller")
}

class Wgs1HaplotypeCallerGvcfTest extends ShivaWgs1 {
  override def variantcallers = List("haplotypecaller_gvcf")
}

class Wgs1UnifiedGenotyperTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
}

class Wgs1BcftoolsTest extends ShivaWgs1 {
  override def variantcallers = List("bcftools")
}

class Wgs1BcftoolsSinglesampleTest extends ShivaWgs1 {
  override def variantcallers = List("bcftools_singlesample")
}

class Wgs1FreebayesTest extends ShivaWgs1 {
  override def variantcallers = List("freebayes")
}

class Wgs1RawTest extends ShivaWgs1 {
  override def variantcallers = List("raw")
}

class Wgs1SampleLibraryCallingTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def multisampleVariantcalling = Some(false)
  override def libraryVariantcalling = Some(true)
  override def singleSampleVariantcalling = Some(true)
}

class Wgs1NoBaserecalTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def useBaseRecalibration = Some(false)
}

class Wgs1NoIndelRealignTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
}

class Wgs1NoPreprocessTest extends ShivaWgs1 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
  override def useBaseRecalibration = Some(false)
}

