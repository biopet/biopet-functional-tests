package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.{Biopet, Samples}

/**
 * Created by pjvanthof on 01/11/15.
 */
class ShivaBiopetplanet30xTest extends ShivaSuccess {
  override def referenceSpecies = Some("H.sapiens")
  override def referenceName = Some("GRCh37")

  override def variantcallers = List("haplotypecaller" ,"haplotypecaller_gvcf", "unifiedgenotyper")

  override def configs = super.configs ::: Samples.na12878Gatc30xConfig :: Nil

  override def functionalTest = true

  def samples = Map("NA12878" -> List("biopetplanet-30x"))

  override def ampliconBed = Some(Biopet.fixtureFile(List("samples", "NA12878", "biopetplanet-30x", "gcat_set_025.GRCh37.bed").mkString(File.separator)))

  override def referenceVcf = Some(Biopet.fixtureFile(List("samples", "NA12878", "NISTIntegratedCalls_14datasets_131103_allcall_UGHapMerge_HetHomVarPASS_VQSRv2.19_2mindatasets_5minYesNoRatio_all_nouncert_excludesimplerep_excludesegdups_excludedecoy_excludeRepSeqSTRs_noCNVs.vcf.gz").mkString(File.separator)))
  override def referenceVcfRegions = Some(Biopet.fixtureFile(List("samples", "NA12878", "biopetplanet-30x", "gcat_set_025.GRCh37.confidence.bed").mkString(File.separator)))
}
