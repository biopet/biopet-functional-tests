package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.shiva.variantcallers._
import nl.lumc.sasc.biopet.test.{ TestReference, Biopet }

/**
 * Created by pjvan_thof on 10/22/15.
 */
trait ShivaVariantcallingWgs1 extends ShivaVariantcallingSuccess with TestReference {
  override def bamFiles = List(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "wgs1.bam"))
}

class HaplotypeCallerTest extends ShivaVariantcallingWgs1 with Haplotypecaller

class HaplotypeCallerGvcfTest extends ShivaVariantcallingWgs1 with HaplotypecallerGvcf

class UnifiedGenotyperTest extends ShivaVariantcallingWgs1 with Unifiedgenotyper

class BcftoolsTest extends ShivaVariantcallingWgs1 with Bcftools

class BcftoolsSingleSampleTest extends ShivaVariantcallingWgs1 with BcftoolsSinglesample

class FreebayesTest extends ShivaVariantcallingWgs1 with Freebayes

class RawTest extends ShivaVariantcallingWgs1 with Raw
