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

  override def functionalTest =
    gearsUseKraken.getOrElse(false) || gearsUseCentrifuge.getOrElse(true)

  def skipFlexiprep: Option[Boolean] = None
  def gearsUseKraken: Option[Boolean] = None
  def gearsUseCentrifuge: Option[Boolean] = None
  def gearUseQiimeRtax: Option[Boolean] = None
  def gearUseQiimeClosed: Option[Boolean] = None
  def gearUseQiimeOpen: Option[Boolean] = None
  def gearUseSeqCount: Option[Boolean] = None
  def qiimeMultisampleOpenReference: Option[Boolean] = None

  override def args =
    super.args ++
      cmdConfig("skip_flexiprep", skipFlexiprep) ++
      cmdConfig("gears_use_kraken", gearsUseKraken) ++
      cmdConfig("gears_use_centrifuge", gearsUseCentrifuge) ++
      cmdConfig("gears_use_qiime_rtax", gearUseQiimeRtax) ++
      cmdConfig("gears_use_qiime_closed", gearUseQiimeClosed) ++
      cmdConfig("gears_use_qiime_open", gearUseQiimeOpen) ++
      cmdConfig("gears_use_seq_count", gearUseSeqCount) ++
      cmdConfig("qiime_multisample_open_reference", qiimeMultisampleOpenReference)
}
