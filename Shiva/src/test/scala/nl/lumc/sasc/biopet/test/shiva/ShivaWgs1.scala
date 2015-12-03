package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.samples.Wgs1
import nl.lumc.sasc.biopet.test.shiva.variantcallers._

/**
 * Created by pjvan_thof on 10/1/15.
 */
trait ShivaWgs1 extends ShivaSuccess with BwaMem with TestReference with Wgs1

class Wgs1HaplotypeCallerTest extends ShivaWgs1 with Haplotypecaller

class Wgs1HaplotypeCallerGvcfTest extends ShivaWgs1 with HaplotypecallerGvcf

class Wgs1UnifiedGenotyperTest extends ShivaWgs1 with Unifiedgenotyper

class Wgs1BcftoolsTest extends ShivaWgs1 with Bcftools

class Wgs1BcftoolsSinglesampleTest extends ShivaWgs1 with BcftoolsSinglesample

class Wgs1FreebayesTest extends ShivaWgs1 with Freebayes

class Wgs1RawTest extends ShivaWgs1 with Raw

class Wgs1SampleLibraryCallingTest extends ShivaWgs1 with Unifiedgenotyper {
  override def multisampleVariantcalling = Some(false)
  override def libraryVariantcalling = Some(true)
  override def singleSampleVariantcalling = Some(true)
}

class Wgs1NoBaserecalTest extends ShivaWgs1 with Unifiedgenotyper {
  override def useBaseRecalibration = Some(false)
}

class Wgs1NoIndelRealignTest extends ShivaWgs1 with Unifiedgenotyper {
  override def useIndelRealigner = Some(false)
}

class Wgs1NoPreprocessTest extends ShivaWgs1 with Unifiedgenotyper {
  override def useIndelRealigner = Some(false)
  override def useBaseRecalibration = Some(false)
}

class Wgs1RegionBed extends ShivaWgs1 with Unifiedgenotyper {
  override def ampliconBed = Some(Biopet.fixtureFile("reference", "target.bed"))
}

class Wgs1Normalize extends ShivaWgs1 with Unifiedgenotyper {
  override def executeVtNormalize = Some(true)
}

class Wgs1Decompose extends ShivaWgs1 with Unifiedgenotyper {
  override def executeVtDecompose = Some(true)
}

class Wgs1NormalizeDecompose extends ShivaWgs1 with Unifiedgenotyper {
  override def executeVtNormalize = Some(true)
  override def executeVtDecompose = Some(true)
}