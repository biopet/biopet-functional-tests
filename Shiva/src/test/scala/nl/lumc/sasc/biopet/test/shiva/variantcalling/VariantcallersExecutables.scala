package nl.lumc.sasc.biopet.test.shiva.variantcalling

import nl.lumc.sasc.biopet.test.SummaryPipeline
import nl.lumc.sasc.biopet.test.shiva.Shiva
import nl.lumc.sasc.biopet.test.utils._
import org.testng.annotations.DataProvider

/**
 * Created by pjvan_thof on 10/20/15.
 */
trait VariantcallersExecutables extends SummaryPipeline {
  if (variantcallers.contains("haplotypecaller") ||
    variantcallers.contains("haplotypecaller_gvcf") ||
    variantcallers.contains("haplotypecaller_allele")) addExecutable(Executable("haplotypecaller", Some(""".+""".r)))
  else addNotHavingExecutable("haplotypecaller")

  if (variantcallers.contains("unifiedgenotyper") ||
    variantcallers.contains("unifiedgenotyper_allele")) addExecutable(Executable("unifiedgenotyper", Some(""".+""".r)))
  else addNotHavingExecutable("unifiedgenotyper")

  if (variantcallers.contains("freebayes")) addExecutable(Executable("freebayes", Some(""".+""".r)))
  else addNotHavingExecutable("freebayes")

  if (variantcallers.contains("bcftools") ||
    variantcallers.contains("bcftools_singlesample")) addExecutable(Executable("bcftoolscall", Some(""".+""".r)))
  else addNotHavingExecutable("bcftools")

  if (variantcallers.contains("raw")) addExecutable(Executable("mpileuptovcf", Some(""".+""".r)))
  else addNotHavingExecutable("mpileuptovcf")

  if (variantcallers.contains("bcftools") ||
    variantcallers.contains("bcftools_singlesample") ||
    variantcallers.contains("raw")) addExecutable(Executable("samtoolsmpileup", Some(""".+""".r)))
  else addNotHavingExecutable("samtoolsmpileup")

  def variantcallers: List[String]
}
