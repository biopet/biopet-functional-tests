package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references._
import nl.lumc.sasc.biopet.test.samples.NA12878Bioplanet30x
import nl.lumc.sasc.biopet.test.shiva.variantcallers._
import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvanthof on 01/11/15.
 */
class ShivaBiopetplanet30xGRCh37AnalysisTest extends ShivaSuccess with BwaMem with HsapiensGRCh37_p13_no_alt_analysis_set with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def minPrecision = 0.945
  override def minRecall = 0.945

  override def memoryArg = "-Xmx512m"

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
  override def memoryArg = "-Xmx512m"
}

class ShivaBiopetplanet30xGRCh38Test extends ShivaSuccess with BwaMem with HsapiensGRCh38 with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def disablescatter = false
  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)
  override def vepVersion = Some("86")
  override def memoryArg = "-Xmx512m"
}

class ShivaBiopetplanet30xGRCh37Test extends ShivaSuccess with BwaMem with HsapiensGRCh37 with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def disablescatter = false
  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)
  override def vepVersion = Some("86")
}

class ShivaBiopetplanet30xHg19Test extends ShivaSuccess with BwaMem with HsapiensHg19 with NA12878Bioplanet30x
  with Haplotypecaller with HaplotypecallerGvcf with Unifiedgenotyper {
  override def disablescatter = false
  def paired = true
  def shouldHaveKmerContent = Some(false)
  override def annotation = Some(true)
  override def vepVersion = Some("86")
  override def memoryArg = "-Xmx512m"
}
