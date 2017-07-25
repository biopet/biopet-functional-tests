package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.aligners.Star
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.samples.Rna3

/**
  * Created by pjvan_thof on 25-7-17.
  */
class GentrapTest
    extends GentrapSuccess
    with Rna3
    with Star
    with TestReference
    with AllExpressionMeasures {
  override def strandProtocol: Option[String] = Some("non_specific")

  override def annotationRefflat: Option[File] =
    Some(Biopet.fixtureFile("reference", "reference.refflat"))

  def ribosomalRefflat: Option[File] = None

  def annotationGtf: Option[File] =
    Some(Biopet.fixtureFile("reference", "reference.gtf"))

  def paired: Boolean = true

  def shouldHaveKmerContent: Option[Boolean] = None
}
