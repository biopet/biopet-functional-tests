package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.samples.{ Wgs1, Wgs2 }
import nl.lumc.sasc.biopet.test.shiva.variantcallers._
import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvan_thof on 10/23/15.
 */
trait ShivaWgs1Wgs2 extends ShivaSuccess with BwaMem with TestReference with Wgs1 with Wgs2 {
  def paired = true
}

class Wgs1Wgs2HaplotypeCallerTest extends ShivaWgs1Wgs2 with Haplotypecaller

class Wgs1Wgs2HaplotypeCallerGvcfTest extends ShivaWgs1Wgs2 with HaplotypecallerGvcf

class Wgs1Wgs2UnifiedGenotyperTest extends ShivaWgs1Wgs2 with Unifiedgenotyper

class Wgs1Wgs2BcftoolsTest extends ShivaWgs1Wgs2 with Bcftools

class Wgs1Wgs2BcftoolsSinglesampleTest extends ShivaWgs1Wgs2 with BcftoolsSinglesample

class Wgs1Wgs2FreebayesTest extends ShivaWgs1Wgs2 with Freebayes

class Wgs1Wgs2RawTest extends ShivaWgs1Wgs2 with Raw

class Wgs1Wgs2VarscanCnsSinglesampleTest extends ShivaWgs1Wgs2 with VarscanCnsSinglesample

class Wgs1Wgs2SampleLibraryCallingTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def multisampleVariantcalling = Some(false)
  override def libraryVariantcalling = Some(true)
  override def singleSampleVariantcalling = Some(true)
}

class Wgs1Wgs2NoBaserecalTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def useBaseRecalibration = Some(false)
}

class Wgs1Wgs2NoIndelRealignTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def useIndelRealigner = Some(false)
}

class Wgs1Wgs2NoPreprocessTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def useIndelRealigner = Some(false)
  override def useBaseRecalibration = Some(false)
}

class Wgs1Wgs2RegionBedTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def ampliconBed = Some(Biopet.fixtureFile("reference", "target.bed"))
}

class Wgs1Wgs2NormalizeTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def executeVtNormalize = Some(true)
}

class Wgs1Wgs2DecomposeTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def executeVtDecompose = Some(true)
}

class Wgs1Wgs2NormalizeDecomposeTest extends ShivaWgs1Wgs2 with Unifiedgenotyper {
  override def executeVtNormalize = Some(true)
  override def executeVtDecompose = Some(true)
}