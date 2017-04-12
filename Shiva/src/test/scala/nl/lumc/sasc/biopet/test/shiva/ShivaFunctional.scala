package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references._
import nl.lumc.sasc.biopet.test.samples.NA12878Bioplanet30x
import nl.lumc.sasc.biopet.test.samples.NA12878WGS
import nl.lumc.sasc.biopet.test.shiva.variantcallers._
import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.shiva.svcallers._
import nl.lumc.sasc.biopet.test.shiva.svcalling.ShivaSvCalling
import nl.lumc.sasc.biopet.test.utils.createTempConfig

/**
 * Created by pjvanthof on 01/11/15.
 */
class ShivaBiopetplanet30xGRCh37AnalysisTest extends ShivaSuccess with BwaMem with HsapiensGRCh37_p13_no_alt_analysis_set with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def minPrecision = 0.945
  override def minRecall = 0.945

  override def memoryArg = "-Xmx1G"

  override def vepVersion = Some("86")

  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)

  override def disablescatter = false

  override def ampliconBed =
    Some(Biopet.fixtureFile(List("samples", "NA12878", "biopetplanet-30x", "gcat_set_025.bed")
      .mkString(File.separator)))

  override def referenceVcf =
    Some(Biopet.fixtureFile(List("samples", "NA12878", "giab.v2.19.vcf.gz")
      .mkString(File.separator)))

  override def referenceVcfRegions =
    Some(Biopet.fixtureFile(List("samples", "NA12878", "biopetplanet-30x", "gcat_set_025.confidence.bed")
      .mkString(File.separator)))
}

class ShivaBiopetplanet30xGRCh38AnalysisTest extends ShivaSuccess with BwaMem with HsapiensGRCh38_no_alt_analysis_set with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def disablescatter = false
  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)
  override def vepVersion = Some("86")
  override def memoryArg = "-Xmx1G"
}

class ShivaBiopetplanet30xGRCh38Test extends ShivaSuccess with BwaMem with HsapiensGRCh38 with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def disablescatter = false
  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)
  override def vepVersion = Some("86")
  override def memoryArg = "-Xmx1G"
}

class ShivaBiopetplanet30xGRCh37Test extends ShivaSuccess with BwaMem with HsapiensGRCh37 with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def disablescatter = false
  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)
  override def vepVersion = Some("86")
  override def memoryArg = "-Xmx1G"
}

class ShivaBiopetplanet30xHg19Test extends ShivaSuccess with BwaMem with HsapiensHg19 with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def disablescatter = false
  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)
  override def vepVersion = Some("86")
  override def memoryArg = "-Xmx1G"
}

class ShivaNA12878WGSGRCh38Test extends ShivaSuccess with BwaMem with HsapiensGRCh38_no_alt_analysis_set with NA12878WGS
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {

  override def disablescatter = false
  override def memoryArg = "-Xmx1G"

  def paired = true
  def shouldHaveKmerContent = Some(true)

  override def annotation = Some(true)
  override def vepVersion = Some("86")

  override def dbsnpVcfFile = Some(Biopet.fixtureFile("shiva", "dbsnp-149.vcf.gz"))

  override def referenceVcf = Some(Biopet.fixtureFile("samples", "NA12878_wgs", "snp_indel_calls", "GIAB_GRCh38_v3.3.2.vcf.gz"))
  override def referenceVcfRegions = Some(Biopet.fixtureFile("samples", "NA12878_wgs", "snp_indel_calls", "GIAB_GRCh38_regions_v3.3.2.bed"))

  override def svCalling = Some(true)
  val svCallers: List[String] = ShivaSvCalling.getSvCallersAsStrList(List(new Breakdancer, new Clever, new Delly))

  override def configs = super.configs.::(createTempConfig(Map("sv_callers" -> svCallers)))

}
