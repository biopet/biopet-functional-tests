package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline

import scala.math._

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

  def skipMarkDuplicates: Option[Boolean] = None
  def skipFlexiprep: Option[Boolean] = None
  def skipMetrics: Option[Boolean] = None

  def paired = r2.isDefined

  def configNumberChunks: Option[Int] = None
  def configChunking = Option(false)
  def configChunksize: Option[Int] = None

  def chunking = configChunking match {
    case Some(c) => c
    case _       => configNumberChunks.exists(_ > 1)
  }

  def numberChunks: Option[Int] = {
    if (r1.isDefined && chunking) {
      configNumberChunks match {
        case Some(_) => configNumberChunks
        case _ =>
          val fileSize = r1.get.length()
          val size = if (r1.get.getName.endsWith(".gz") || r1.get.getName.endsWith(".gzip")) fileSize * 3 else fileSize
          Some(ceil(size.toDouble / configChunksize.getOrElse(1 << 30)).toInt)
      }
    } else None
  }

  def readgroupId: Option[String] = None
  def readgroupDescription: Option[String] = None
  def platformUnit: Option[String] = None
  def predictedInsertsize: Option[Int] = None
  def readgroupSequencingCenter: Option[String] = None
  def platform: Option[String] = None

  def generateWig: Option[Boolean] = None
  def chunkMetrics: Option[Boolean] = None

  def args = sampleId.collect { case sampleId => Seq("-sample", sampleId) }.getOrElse(Seq()) ++
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
    }) ++ (configChunking match {
      case Some(true)  => Seq("-cv", "chunking=true")
      case Some(false) => Seq("-cv", "chunking=false")
      case _           => Seq()
    }) ++ (generateWig match {
      case Some(true)  => Seq("-cv", "generate_wig=true")
      case Some(false) => Seq("-cv", "generate_wig=false")
      case _           => Seq()
    }) ++ (chunkMetrics match {
      case Some(true)  => Seq("-cv", "chunk_metrics=true")
      case Some(false) => Seq("-cv", "chunk_metrics=false")
      case _           => Seq()
    }) ++
    configNumberChunks.collect { case numberChunks => Seq("-cv", s"number_chunks=$numberChunks") }.getOrElse(Seq()) ++
    configChunksize.collect { case chunksize => Seq("-cv", s"chunksize=$chunksize") }.getOrElse(Seq())

}
