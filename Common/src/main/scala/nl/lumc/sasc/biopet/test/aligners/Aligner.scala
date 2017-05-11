package nl.lumc.sasc.biopet.test.aligners

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.references.Reference

/**
  * Created by pjvanthof on 14/11/15.
  */
trait Aligner extends Pipeline with Reference {
  def aligner: Option[String]
  override def args = super.args ++ cmdConfig("aligner", aligner)
}

trait BwaMem extends Aligner {
  def aligner = Some("bwa-mem")
  def bwaMemFasta: Option[File]
  override def args = super.args ++ cmdConfig("bwamem:reference_fasta", bwaMemFasta)
}

trait Bowtie extends Aligner {
  def aligner = Some("bowtie")
  def bowtieIndex: Option[File]
  override def args = super.args ++ cmdConfig("bowtie:bowtie_index", bowtieIndex)
}

trait Bowtie2 extends Aligner {
  def aligner = Some("bowtie2")
  def bowtie2Index: Option[File]
  override def args = super.args ++ cmdConfig("bowtie2:bowtie_index", bowtie2Index)
}

trait Star extends Aligner {
  def aligner = Some("star")
  def starGenomeDir: Option[File]
  override def args = super.args ++ cmdConfig("star:genomeDir", starGenomeDir)
}

trait Hisat2 extends Aligner {
  def aligner = Some("hisat2")
  def hisat2Index: Option[String]
  override def args = super.args ++ cmdConfig("hisat2:hisat2_index", hisat2Index)
}

trait Gsnap extends Aligner {
  def aligner = Some("gsnap")
  def gsnapDir: Option[File]
  def gsnapDb: Option[String]
  override def args =
    super.args ++ cmdConfig("gsnap:dir", gsnapDir) ++ cmdConfig("gsnap:db", gsnapDb)
}

trait Tophat extends Aligner {
  def aligner = Some("tophat")
  def tophatIndex: Option[String]
  override def args = super.args ++ cmdConfig("tophat:bowtie_index", tophatIndex)
}
