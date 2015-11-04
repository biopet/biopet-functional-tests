package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, Samples }

/**
 * Created by pjvanthof on 01/11/15.
 */
class ShivaBiopetplanet30xTest extends ShivaSuccess {
  override def referenceSpecies = Some("H.sapiens")
  override def referenceName = Some("GRCh37")
  override def minPrecision = 0.945
  override def minRecall = 0.945

  override def variantcallers = List("haplotypecaller", "haplotypecaller_gvcf", "unifiedgenotyper")

  override def configs = super.configs ::: Samples.na12878Gatc30xConfig :: Nil

  override def functionalTest = true

  override def disablescatter = false

  def samples = Map("NA12878" -> List("biopetplanet-30x"))

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
