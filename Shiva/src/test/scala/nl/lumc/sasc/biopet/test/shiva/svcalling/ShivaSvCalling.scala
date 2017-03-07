package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.shiva.svcallers.SvCaller
import nl.lumc.sasc.biopet.test.utils.createTempConfig

trait ShivaSvCalling extends Pipeline {

  override def pipelineName = "shivasvcalling"

  def svCallers: List[SvCaller]

  def bamFile: File

  override def configs = super.configs.::(createTempConfig(Map("sv_callers" -> getSvCallersAsStrList)))

  override def args = super.args ++ Seq("-BAM", bamFile.getAbsolutePath)

  private def getSvCallersAsStrList(): List[String] = {
    var svCallersAsStr: List[String] = List()
    for (svCaller <- svCallers) svCallersAsStr = svCallersAsStr.::(svCaller.toString)
    svCallersAsStr
  }

}
