package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 5/26/15.
 */
trait Gentrap extends Pipeline {

  def pipelineName = "gentrap"

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def summaryFile = new File(outputDir, s"gentrap.summary.json")

  def expressionMeasures: List[String] = Nil

  def strandProtocol: Option[String] = None

  def annotationRefflat: Option[File] = None

  def annotationGtf: Option[File] = None

  def annotationBed: Option[File] = None

  def removeRibosomalReads: Option[Boolean] = None

  def ribosomalRefflat: Option[File] = None

  def callVariants: Option[Boolean] = None

  def expressionMeasuresConfig = if (expressionMeasures.nonEmpty) Some(createTempConfig(Map("expression_measures" -> expressionMeasures))) else None

  override def configs = super.configs ::: expressionMeasuresConfig.map(_ :: Nil).getOrElse(Nil)

  def args = cmdConfig("species", referenceSpecies) ++
    cmdConfig("reference_name", referenceName) ++
    cmdConfig("aligner", aligner) ++
    cmdConfig("strand_protocol", strandProtocol) ++
    cmdConfig("annotation_refflat", annotationRefflat) ++
    cmdConfig("annotation_gtf", annotationGtf) ++
    cmdConfig("annotation_bed", annotationBed) ++
    cmdConfig("remove_ribosomal_reads", removeRibosomalReads) ++
    cmdConfig("ribosomal_refflat", ribosomalRefflat) ++
    cmdConfig("call_variants", callVariants)
}

