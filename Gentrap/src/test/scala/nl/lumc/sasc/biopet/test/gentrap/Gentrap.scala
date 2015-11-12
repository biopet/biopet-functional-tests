package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }, Pipeline._
import nl.lumc.sasc.biopet.test.utils._

trait Gentrap extends Pipeline { this: GentrapAnnotations =>

  def pipelineName = "gentrap"

  def summaryFile = new File(outputDir, s"gentrap.summary.json")

  def removeRibosomalReads: Option[Boolean] = None

  def callVariants: Option[Boolean] = None

  def expressionMeasures: List[String] = Nil

  def strandProtocol: Option[String] = None

  def expressionMeasuresConfig: Option[File] =
    if (expressionMeasures.nonEmpty)
      Option(createTempConfig(Map("expression_measures" -> expressionMeasures), "exp_measures"))
    else None

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def referenceFasta: Option[File] = Option(Biopet.fixtureFile("gentrap", "hg19_mini.fa"))

  def aligner: Option[String]

  override def configs = super.configs ++ expressionMeasuresConfig.toList

  def args = cmdConfig("species", referenceSpecies) ++
    cmdConfig("reference_fasta", referenceFasta) ++
    cmdConfig("reference_name", referenceName) ++
    cmdConfig("strand_protocol", strandProtocol) ++
    cmdConfig("annotation_refflat", annotationRefflat) ++
    cmdConfig("annotation_gtf", annotationGtf) ++
    cmdConfig("annotation_bed", annotationBed) ++
    cmdConfig("remove_ribosomal_reads", removeRibosomalReads) ++
    cmdConfig("ribosomal_refflat", ribosomalRefflat) ++
    cmdConfig("call_variants", callVariants) ++
    cmdConfig("aligner", aligner)
}

trait GentrapAnnotations { this: Gentrap =>
  def annotationRefflat: Option[File]
  def annotationGtf: Option[File]
  def annotationBed: Option[File]
  def ribosomalRefflat: Option[File]
}

trait GentrapRefSeq extends GentrapAnnotations { this: Gentrap =>
  def annotationRefflat = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.refFlat"))
  def annotationGtf = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.gtf"))
  def annotationBed = Option(Biopet.fixtureFile("gentrap", "annotations", "ucsc_refseq.bed"))
  def ribosomalRefflat: Option[File] = None
}

