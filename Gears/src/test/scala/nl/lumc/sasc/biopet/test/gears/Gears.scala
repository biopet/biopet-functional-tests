package nl.lumc.sasc.biopet.test.gears

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by pjvan_thof on 2/2/16.
 */
trait Gears extends Pipeline {
  def pipelineName = "gears"

  def summaryFile = new File(outputDir, s"gears.summary.json")

  override def functionalTest = gearsUseKraken.getOrElse(true)

  def gearsUseKraken: Option[Boolean] = None
  def gearUseQiimeRtax: Option[Boolean] = None
  def gearUseQiimeClosed: Option[Boolean] = None
  def gearUseSeqCount: Option[Boolean] = None

  override def args = super.args ++
    cmdConfig("gears_use_kraken", gearsUseKraken) ++
    cmdConfig("gears_use_qiime_rtax", gearUseQiimeRtax) ++
    cmdConfig("gears_use_qiime_closed", gearUseQiimeClosed) ++
    cmdConfig("gears_use_seq_count", gearUseSeqCount)
}
