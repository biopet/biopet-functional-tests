package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.shiva.variantcallers._
import nl.lumc.sasc.biopet.test.{ Biopet, Samples }

/**
 * Created by pjvan_thof on 10/23/15.
 */
trait ShivaWgs2 extends ShivaSuccess with BwaMem with TestReference {
  override def configs = super.configs ::: Samples.wgs2Config :: Nil
  override def referenceVcf = Some(Biopet.fixtureFile("samples" + File.separator + "wgs2" + File.separator + "wgs2.vcf.gz"))

  def samples = Map("wgs2" -> List("lib1", "lib2"))
}

class Wgs2HaplotypeCallerTest extends ShivaWgs2 with Haplotypecaller

class Wgs2HaplotypeCallerGvcfTest extends ShivaWgs2 with HaplotypecallerGvcf

class Wgs2UnifiedGenotyperTest extends ShivaWgs2 with Unifiedgenotyper

class Wgs2BcftoolsTest extends ShivaWgs2 with Bcftools

class Wgs2BcftoolsSinglesampleTest extends ShivaWgs2 with BcftoolsSinglesample

class Wgs2FreebayesTest extends ShivaWgs2 with Freebayes

class Wgs2RawTest extends ShivaWgs2 with Raw

class Wgs2SampleLibraryCallingTest extends ShivaWgs2 with Unifiedgenotyper {
  override def multisampleVariantcalling = Some(false)
  override def libraryVariantcalling = Some(true)
  override def singleSampleVariantcalling = Some(true)
}

class Wgs2NoBaserecalTest extends ShivaWgs2 with Unifiedgenotyper {
  override def useBaseRecalibration = Some(false)
}

class Wgs2NoIndelRealignTest extends ShivaWgs2 with Unifiedgenotyper {
  override def useIndelRealigner = Some(false)
}

class Wgs2NoPreprocessTest extends ShivaWgs2 with Unifiedgenotyper {
  override def useIndelRealigner = Some(false)
  override def useBaseRecalibration = Some(false)
}

