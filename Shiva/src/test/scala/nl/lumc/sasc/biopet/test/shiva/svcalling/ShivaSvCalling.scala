package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.shiva.svcallers.SvCaller
import nl.lumc.sasc.biopet.test.utils.createTempConfig
import org.testng.annotations.DataProvider

trait ShivaSvCalling extends Pipeline {

  override def pipelineName = "shivasvcalling"

  def svCallers: List[SvCaller]

  def bamFiles: List[File]

  @DataProvider(name = "callers")
  def callersProvider = {
    svCallers.map(Array(_)).toArray
  }

  override def configs = createTempConfig(Map("sv_callers" -> ShivaSvCalling.getSvCallersAsStrList(svCallers))) :: super.configs

  override def args = super.args ++ bamFiles.flatMap(x => Seq("-BAM", x.getAbsolutePath))

}

object ShivaSvCalling {

  def getSvCallersAsStrList(svCallers: List[SvCaller]): List[String] = {
    var svCallersAsStr: List[String] = List()
    for (svCaller <- svCallers) svCallersAsStr = svCallersAsStr.::(svCaller.toString)
    svCallersAsStr
  }

}
