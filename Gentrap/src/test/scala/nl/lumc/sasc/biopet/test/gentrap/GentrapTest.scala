package nl.lumc.sasc.biopet.test.gentrap

import nl.lumc.sasc.biopet.test.Samples

/**
 * Created by pjvanthof on 05/11/15.
 */
class GentrapTest extends GentrapSuccess {
  override def configs = super.configs ::: Samples.wgs1Config :: Nil

  def samples = Map("wgs1" -> List("lib1"))

  override def referenceSpecies = Some("H.sapiens")
  override def referenceName = Some("GRCh38")

  override def expressionMeasures = List(
    "fragments_per_gene", "bases_per_gene", "bases_per_exon",
    "cufflinks_strict", "cufflinks_guided", "cufflinks_blind")

}
