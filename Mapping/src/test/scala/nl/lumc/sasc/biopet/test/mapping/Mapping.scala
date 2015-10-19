package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

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

  def args = cmdArg("-sample", sampleId) ++ cmdArg("-library", libId) ++
    cmdArg("-R1", r1) ++ cmdArg("-R2", r2) ++
    cmdConfig("species", referenceSpecies) ++
    cmdConfig("reference_name", referenceName) ++
    cmdConfig("aligner", aligner) ++
    cmdConfig("skip_markduplicates", skipMarkDuplicates) ++
    cmdConfig("skip_flexiprep", skipFlexiprep) ++
    cmdConfig("skip_metrics", skipMetrics) ++
    cmdConfig("chunking", configChunking) ++
    cmdConfig("generate_wig", generateWig) ++
    cmdConfig("chunk_metrics", chunkMetrics) ++
    cmdConfig("number_chunks", configNumberChunks) ++
    cmdConfig("chunksize", configChunksize)

}
