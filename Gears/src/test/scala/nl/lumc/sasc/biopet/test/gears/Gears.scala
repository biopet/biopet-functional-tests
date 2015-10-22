package nl.lumc.sasc.biopet.test.gears

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by waiyileung on 05-10-15.
 */
trait GearsRun extends Pipeline {
  def summaryFile = new File(outputDir, s"${pipelineName}.summary.json")

  def pipelineName = "gears"

  def paired = r2.isDefined

  override def args = cmdArg("-R1", r1) ++ cmdArg("-R2", r2) ++
    cmdArg("-bam", bam) ++ cmdArg("--outputName", outputName)

  def r1: Option[File] = None

  def r2: Option[File] = None

  def bam: Option[File] = None

  def outputName: Option[String] = None
}
