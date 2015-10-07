package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline

/**
 * Created by pjvan_thof on 5/26/15.
 */
trait Mapping extends Pipeline {

  def pipelineName = "mapping"

  def sampleId = Option("sampleName")

  def libId = Option("libName")

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def r1: Option[File] = None

  def r2: Option[File] = None

  def summaryFile = new File(outputDir, s"${sampleId}-${libId}.summary.json")

  def skipMarkDuplicates: Option[Boolean] = None
  def skipFlexiprep: Option[Boolean] = None
  def skipMetrics: Option[Boolean] = None

  def paired = r2.isDefined

  def numberChunks: Option[Int] = None
  def chunking = Option(false)
  def chunksize: Option[Int] = None

  def args = Seq("-cv", s"output_dir=$outputDir") ++
    sampleId.collect { case sampleId => Seq("-sample", sampleId) }.getOrElse(Seq()) ++
    libId.collect { case libId => Seq("-library", libId) }.getOrElse(Seq()) ++
    referenceSpecies.collect { case species => Seq("-cv", s"species=$species") }.getOrElse(Seq()) ++
    referenceName.collect { case name => Seq("-cv", s"reference_name=$name") }.getOrElse(Seq()) ++
    aligner.collect { case aligner => Seq("-cv", s"aligner=$aligner") }.getOrElse(Seq()) ++
    r1.collect { case r1 => Seq("-R1", r1.getAbsolutePath) }.getOrElse(Seq()) ++
    r2.collect { case r2 => Seq("-R2", r2.getAbsolutePath) }.getOrElse(Seq()) ++
    (skipMarkDuplicates match {
      case Some(true)  => Seq("-cv", "skip_markduplicates=true")
      case Some(false) => Seq("-cv", "skip_markduplicates=false")
      case _           => Seq()
    }) ++
    (skipFlexiprep match {
      case Some(true)  => Seq("-cv", "skip_flexiprep=true")
      case Some(false) => Seq("-cv", "skip_flexiprep=false")
      case _           => Seq()
    }) ++
    (skipMetrics match {
      case Some(true)  => Seq("-cv", "skip_metrics=true")
      case Some(false) => Seq("-cv", "skip_metrics=false")
      case _           => Seq()
    }) ++ (chunking match {
      case Some(true)  => Seq("-cv", "chunking=true")
      case Some(false) => Seq("-cv", "chunking=false")
      case _           => Seq()
    }) ++
    numberChunks.collect { case numberChunks => Seq("-cv", s"number_chunks=$numberChunks") }.getOrElse(Seq()) ++
    chunksize.collect { case chunksize => Seq("-cv", s"chunksize=$chunksize") }.getOrElse(Seq())

}
