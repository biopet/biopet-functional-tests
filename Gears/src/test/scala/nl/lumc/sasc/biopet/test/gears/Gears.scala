package nl.lumc.sasc.biopet.test.gears

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by waiyileung on 05-10-15.
 */
trait Gears extends Pipeline {
  def pipelineName = "gears"

  def paired = r2.isDefined

  def sampleId = Option("sampleName")

  def libId = None

  def r1: Option[File] = None

  def r2: Option[File] = None

  def bam: Option[File] = None

  def outputName: Option[String] = None

  override def args = cmdArg("-sample", sampleId) ++ cmdArg("-library", libId) ++
    cmdArg("-R1", r1) ++ cmdArg("-R2", r2) ++
    cmdArg("-bam", bam) ++ cmdArg("--outputName", outputName)

}
