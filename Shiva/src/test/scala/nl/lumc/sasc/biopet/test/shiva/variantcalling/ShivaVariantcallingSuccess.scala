package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.SummaryPipeline
import nl.lumc.sasc.biopet.test.shiva.Shiva
import org.json4s._
import org.testng.annotations.{Test, DataProvider}

/**
 * Created by pjvan_thof on 10/16/15.
 */
trait ShivaVariantcallingSuccess extends ShivaVariantcalling with SummaryPipeline {
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

  @Test(dataProvider = "variantcallers", dependsOnGroups = Array("parseSummary"))
  def testVariantcaller(variantcaller: String): Unit = {
    val dir = new File(outputDir, variantcaller)
    val vcfstats = this.summary \ "shivavariantcalling" \ s"${namePrefix.get}-vcfstats-$variantcaller"
    if (variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      vcfstats shouldBe a[JObject]
    } else {
      assert(!dir.exists())
      vcfstats shouldBe JNothing
    }
  }

}
