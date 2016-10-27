package nl.lumc.sasc.biopet.test.references

import java.io.File

/**
 * Created by pjvanthof on 14/11/15.
 */
trait MmusculusGRCm38 extends GlobalReference {
  def referenceSpecies = Some("M.musculus")
  def referenceName = Some("GRCm38")

}
