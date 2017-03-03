package nl.lumc.sasc.biopet.test.shiva.svcalling

import nl.lumc.sasc.biopet.test.Pipeline.cmdConfig
import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.shiva.svcallers.SvCaller

trait ShivaSvCalling extends Pipeline {

  override def pipelineName = "shivasvcalling"

  def svCaller: SvCaller

  override def args = cmdConfig("sv_callers", svCaller.svCallerName)

}
