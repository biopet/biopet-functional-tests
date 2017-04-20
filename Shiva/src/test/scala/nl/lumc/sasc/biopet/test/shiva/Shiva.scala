package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.MultisampleMapping
import nl.lumc.sasc.biopet.test.aligners.Aligner
import nl.lumc.sasc.biopet.test.references.Reference
import nl.lumc.sasc.biopet.test.shiva.variantcallers.Variantcallers
import org.scalatest.Matchers
import scala.collection.JavaConversions._

/**
 * Created by pjvan_thof on 5/26/15.
 */
trait Shiva extends MultisampleMapping with Reference with Aligner with Variantcallers {

  def pipelineName = "shiva"

  def summaryFile = new File(outputDir, s"Shiva.summary.json")

  def libraryVariantcalling: Option[Boolean] = None

  def singleSampleVariantcalling: Option[Boolean] = None

  def multisampleVariantcalling: Option[Boolean] = None

  def svCalling: Option[Boolean] = None

  def useIndelRealigner: Option[Boolean] = None

  def useBaseRecalibration: Option[Boolean] = None

  def useAnalyzeCovariates: Option[Boolean] = None

  def usePrintReads: Option[Boolean] = None

  def dbsnpVcfFile: Option[File] = None

  def referenceVcf: Option[File] = None

  def referenceVcfRegions: Option[File] = None

  def annotation: Option[Boolean] = None

  def ampliconBed: Option[File] = None

  def executeVtNormalize: Option[Boolean] = None

  def executeVtDecompose: Option[Boolean] = None

  def vepVersion: Option[String] = None

  override def args = super.args ++
    cmdConfig("library_variantcalling", libraryVariantcalling) ++
    cmdConfig("single_sample_variantcalling", singleSampleVariantcalling) ++
    cmdConfig("multisample_variantcalling", multisampleVariantcalling) ++
    cmdConfig("sv_calling", svCalling) ++
    cmdConfig("use_indel_realigner", useIndelRealigner) ++
    cmdConfig("use_base_recalibration", useBaseRecalibration) ++
    cmdConfig("use_analyze_covariates", useAnalyzeCovariates) ++
    cmdConfig("annotation", annotation) ++
    cmdConfig("dbsnp_vcf", dbsnpVcfFile) ++
    cmdConfig("reference_vcf", referenceVcf) ++
    cmdConfig("reference_vcf_regions", referenceVcfRegions) ++
    cmdConfig("amplicon_bed", ampliconBed) ++
    cmdConfig("execute_vt_normalize", executeVtNormalize) ++
    cmdConfig("execute_vt_decompose", executeVtDecompose) ++
    cmdConfig("vep_version", vepVersion) ++
    cmdConfig("use_printreads", usePrintReads)
}

object Shiva extends Matchers {
  val validVariantcallers = List("freebayes", "raw", "bcftools", "bcftools_singlesample",
    "haplotypecaller", "unifiedgenotyper", "haplotypecaller_gvcf",
    "haplotypecaller_allele", "unifiedgenotyper_allele")

  def testSamplesVcfFile(file: File, samples: List[String]): Unit = {
    val reader = new VCFFileReader(file, false)
    val vcfSamples = reader.getFileHeader.getSampleNamesInOrder.toList
    samples.foreach { sample => assert(vcfSamples.contains(sample), s"sample '$sample' is not found in $file") }
    samples.size shouldBe vcfSamples.size
    reader.close()
  }
}