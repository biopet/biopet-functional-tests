package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline.cmdConfig
import nl.lumc.sasc.biopet.test.{ Biopet, Pipeline }
import nl.lumc.sasc.biopet.test.shiva.svcallers.SvCaller

trait ShivaSvCalling extends Pipeline {

  override def pipelineName = "shivasvcalling"

  def svCallers: List[SvCaller]

  def bamFile: File

  override def args = super.args ++ Seq("-BAM", bamFile.getAbsolutePath) ++
    cmdConfig("sv_callers", svCallers.mkString("[", ",", "]"))

}
