package nl.lumc.sasc.biopet.test.annotations

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline, Pipeline._

/**
 * Copyright (c) 2015 Leiden University Medical Center - Sequencing Analysis Support Core <sasc@lumc.nl>
 *
 * @author Wibowo Arindrarto <w.arindrarto@lumc.nl>
 */

trait RefflatAnnotation extends Pipeline {
  def annotationRefflat: Option[File]
  override def args = super.args ++ cmdConfig("annotation_refflat", annotationRefflat)
}

trait GtfAnnotation extends Pipeline {
  def annotationGtf: Option[File]
  override def args = super.args ++ cmdConfig("annotation_gtf", annotationGtf)
}

trait BedAnnotation extends Pipeline {
  def annotationBed: Option[File]
  override def args = super.args ++ cmdConfig("annotation_bed", annotationBed)
}

trait GffAnnotation extends Pipeline {
  def annotationGff: Option[File]
  override def args = super.args ++ cmdConfig("annotation_gff", annotationGff)
}

trait RibosomalRefflatAnnotation extends Pipeline {
  def ribosomalRefflat: Option[File]
  override def args = super.args ++ cmdConfig("annotation_ribosomal", ribosomalRefflat)
}
