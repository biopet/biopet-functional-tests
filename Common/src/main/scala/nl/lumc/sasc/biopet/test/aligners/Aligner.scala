package nl.lumc.sasc.biopet.test.aligners

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.references.Reference

import scala.reflect.io.File

/**
 * Created by pjvanthof on 14/11/15.
 */
trait Aligner extends Pipeline with Reference {
  def aligner: Option[String]
  abstract override def args = super.args ++ cmdConfig("aligner", aligner)
}

trait BwaMem extends Aligner {
  def aligner = Some("bwa-mem")
  def bwaMemFasta: Option[File]
  abstract override def args = super.args ++ cmdConfig("bwamem:reference_fasta", bwaMemFasta)
}

trait Bowtie extends Aligner {
  def aligner = Some("bowtie")
  def bowtieFasta: Option[File]
  abstract override def args = super.args ++ cmdConfig("bowtie:reference_fasta", bowtieFasta)
}

trait Star extends Aligner {
  def aligner = Some("star")
  def starGenomeDir: Option[File]
  abstract override def args = super.args ++ cmdConfig("star:genomeDir", starGenomeDir)
}

trait Gsnap extends Aligner {
  def aligner = Some("gsnap")
  def gsnapDir: Option[File]
  def gsnapDb: Option[String]
  abstract override def args = super.args ++ cmdConfig("gsnap:dir", gsnapDir) ++ cmdConfig("gesnap:db", gsnapDb)
}

trait Tophat extends Aligner {
  def aligner = Some("tophat")
  def tophatIndex: Option[String]
  abstract override def args = super.args ++ cmdConfig("tophat:bowtie_index", tophatIndex)
}