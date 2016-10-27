package nl.lumc.sasc.biopet.test.references

import java.io.File

/**
 * Created by pjvanthof on 14/11/15.
 */
trait HsapiensGRCh38 extends GlobalReference {
  def referenceSpecies = Some("H.sapiens")
  def referenceName = Some("GRCh38")

}

