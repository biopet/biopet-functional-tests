package nl.lumc.sasc.biopet.test.shiva

import java.io.{ PrintWriter, File }

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 5/26/15.
 */
trait Shiva extends Pipeline {

  def pipelineName = "shiva"

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def summaryFile = new File(outputDir, s"Shiva.summary.json")

  def bamToFastq: Option[Boolean] = None

  def correctReadgroups: Option[Boolean] = None

  def libraryVariantcalling: Option[Boolean] = None

  def singleSampleVariantcalling: Option[Boolean] = None

  def multisampleVariantcalling: Option[Boolean] = None

  def svCalling: Option[Boolean] = None

  def useIndelRealigner: Option[Boolean] = None

  def useBaseRecalibration: Option[Boolean] = None

  def useAnalyzeCovariates: Option[Boolean] = None

  def dbsnpVcfFile: Option[File] = None

  def referenceVcf: Option[File] = None

  def referenceVcfRegions: Option[File] = None

  def variantcallers: List[String] = Nil

  def annotation: Option[Boolean] = None

  val variantcallersConfig = if (variantcallers.nonEmpty) Some(createTempConfig(Map("variantcallers" -> variantcallers))) else None

  override def configs = super.configs ::: variantcallersConfig.map(_ :: Nil).getOrElse(Nil)

  def args = cmdConfig("species", referenceSpecies) ++
    cmdConfig("reference_name", referenceName) ++
    cmdConfig("aligner", aligner) ++
    cmdConfig("bam_to_fastq", bamToFastq) ++
    cmdConfig("correct_readgroups", correctReadgroups) ++
    cmdConfig("library_variantcalling", libraryVariantcalling) ++
    cmdConfig("single_sample_variantcalling", singleSampleVariantcalling) ++
    cmdConfig("multisample_variantcalling", multisampleVariantcalling) ++
    cmdConfig("sv_calling", svCalling) ++
    cmdConfig("use_indel_realigner", useIndelRealigner) ++
    cmdConfig("use_base_recalibration", useBaseRecalibration) ++
    cmdConfig("use_analyze_covariates", useAnalyzeCovariates) ++
    cmdConfig("annotation", annotation) ++
    cmdConfig("dbsnp", dbsnpVcfFile) ++
    cmdConfig("reference_vcf", referenceVcf) ++
    cmdConfig("reference_vcf_regions", referenceVcfRegions)
}

object Shiva {
  val validVariantcallers = List("freebayes", "raw", "bcftools", "bcftools_singlesample",
    "haplotypecaller", "unifiedgenotyper", "haplotypecaller_gvcf",
    "haplotypecaller_allele", "unifiedgenotyper_allele")
}