package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, Samples, TestReference }
import org.json4s._
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 10/23/15.
 */
trait ShivaWgs1Wgs2 extends ShivaSuccess with TestReference {
  override def configs = super.configs ::: Samples.wgs1Config :: Samples.wgs2Config :: Nil
  override def referenceVcf = Some(Biopet.fixtureFile("shiva" + File.separator + "wgs1.wgs2.vcf.gz"))

  @Test(dependsOnGroups = Array("parseSummary"))
  def testMultisampleConcordance: Unit = {
    val concordance = summary \ "shivavariantcalling" \ "stats" \ "multisample-genotype_concordance-final"
    if (!multisampleVariantcalling.contains(false) && referenceVcf.isDefined) {
      concordance shouldBe a[JObject]
      (concordance \ "genotypeSummary" \ "Overall_Genotype_Concordance").extract[Double] should be > 0.9
    } else {
      concordance shouldBe JNothing
    }
  }

  def samples = Map("wgs1" -> List("lib1"), "wgs2" -> List("lib1", "lib2"))
}

class Wgs1Wgs2HaplotypeCallerTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("haplotypecaller")
}

class Wgs1Wgs2HaplotypeCallerGvcfTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("haplotypecaller_gvcf")
}

class Wgs1Wgs2UnifiedGenotyperTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("unifiedgenotyper")
}

class Wgs1Wgs2BcftoolsTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("bcftools")
}

class Wgs1Wgs2BcftoolsSinglesampleTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("bcftools_singlesample")
}

class Wgs1Wgs2FreebayesTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("freebayes")
}

class Wgs1Wgs2RawTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("raw")
}

class Wgs1Wgs2SampleLibraryCallingTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def multisampleVariantcalling = Some(false)
  override def libraryVariantcalling = Some(true)
  override def singleSampleVariantcalling = Some(true)
}

class Wgs1Wgs2NoBaserecalTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def useBaseRecalibration = Some(false)
}

class Wgs1Wgs2NoIndelRealignTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
}

class Wgs1Wgs2NoPreprocessTest extends ShivaWgs1Wgs2 {
  override def variantcallers = List("unifiedgenotyper")
  override def useIndelRealigner = Some(false)
  override def useBaseRecalibration = Some(false)
}

