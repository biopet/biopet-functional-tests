package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleSuccess
import org.json4s._
import org.testng.annotations.{ DataProvider, Test }

/**
 * Created by pjvan_thof on 9/17/15.
 */
trait ShivaSuccess extends Shiva with MultisampleSuccess {
  if (variantcallers.contains("haplotypecaller") ||
    variantcallers.contains("haplotypecaller_gvcf") ||
    variantcallers.contains("haplotypecaller_allele")) addExecutable(Executable("haplotypecaller", Some(""".+""".r)))
  else addNotExecutable("haplotypecaller")

  if (variantcallers.contains("unifiedgenotyper") ||
    variantcallers.contains("unifiedgenotyper_allele")) addExecutable(Executable("unifiedgenotyper", Some(""".+""".r)))
  else addNotExecutable("unifiedgenotyper")

  if (variantcallers.contains("freebayes")) addExecutable(Executable("freebayes", Some(""".+""".r)))
  else addNotExecutable("freebayes")

  if (variantcallers.contains("bcftools") ||
    variantcallers.contains("bcftools_singlesample")) addExecutable(Executable("bcftools", Some(""".+""".r)))
  else addNotExecutable("bcftools")

  if (variantcallers.contains("raw")) addExecutable(Executable("mpileuptovcf", Some(""".+""".r)))
  else addNotExecutable("mpileuptovcf")

  if (variantcallers.contains("bcftools") ||
    variantcallers.contains("bcftools_singlesample") ||
    variantcallers.contains("raw")) addExecutable(Executable("samtoolsmpileup", Some(""".+""".r)))
  else addNotExecutable("samtoolsmpileup")

  @DataProvider(name = "variantcallers")
  def variantcallerProvider = Shiva.validVariantcallers.map(Array(_)).toArray

  @DataProvider(name = "sample-variantcallers")
  def sampleVariantcallers = {
    (for (sample <- samples.keys; variantcaller <- Shiva.validVariantcallers) yield Array(sample, variantcaller)).toArray
  }

  @DataProvider(name = "library-variantcallers")
  def libraryVariantcallers = {
    (for ((sample, libs) <- samples; lib <- libs; variantcaller <- Shiva.validVariantcallers) yield Array(sample, lib, variantcaller)).toArray
  }

  @Test(dataProvider = "variantcallers", dependsOnGroups = Array("parseSummary"))
  def testVariantcaller(variantcaller: String): Unit = {
    val dir = new File(outputDir, "variantcalling" + File.separator + variantcaller)
    val vcfstats = this.summary \ "shivavariantcalling" \ s"multisample-vcfstats-$variantcaller"
    if (!multisampleVariantcalling.contains(false) && variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      vcfstats shouldBe a[JObject]
    } else {
      assert(!dir.exists())
      vcfstats shouldBe JNothing
    }
  }

  @Test(dataProvider = "sample-variantcallers", dependsOnGroups = Array("parseSummary"))
  def testSampleVariantcaller(sample: String, variantcaller: String): Unit = {
    val dir = new File(sampleDir(sample), "variantcalling" + File.separator + variantcaller)
    val vcfstats = this.summary \ "samples" \ sample \ "shivavariantcalling" \ s"$sample-vcfstats-$variantcaller"
    if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      vcfstats shouldBe a[JObject]
    } else {
      assert(!dir.exists())
      vcfstats shouldBe JNothing
    }
  }

  @Test(dataProvider = "library-variantcallers", dependsOnGroups = Array("parseSummary"))
  def testLibraryVariantcaller(sample: String, lib: String, variantcaller: String): Unit = {
    val dir = new File(libraryDir(sample, lib), "variantcalling" + File.separator + variantcaller)
    val vcfstats = this.summary \ "samples" \ sample \ "libraries" \ lib \ "shivavariantcalling" \ s"$sample-$lib-vcfstats-$variantcaller"
    if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      vcfstats shouldBe a[JObject]
    } else {
      assert(!dir.exists())
      vcfstats shouldBe JNothing
    }
  }
}
