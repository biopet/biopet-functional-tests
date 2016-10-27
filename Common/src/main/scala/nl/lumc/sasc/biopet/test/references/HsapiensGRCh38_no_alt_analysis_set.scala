package nl.lumc.sasc.biopet.test.references

import java.io.File

/**
 * Created by pjvanthof on 14/11/15.
 */
trait HsapiensGRCh38_no_alt_analysis_set extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh38_no_alt_analysis_set")

}

