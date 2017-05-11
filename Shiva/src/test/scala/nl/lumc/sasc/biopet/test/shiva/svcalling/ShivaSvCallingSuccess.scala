package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import nl.lumc.sasc.biopet.test.{SummaryGroup, SummaryPipeline}
import nl.lumc.sasc.biopet.test.shiva.svcallers.{Breakdancer, Clever, Delly, SvCaller}
import org.testng.annotations.Test

trait ShivaSvCallingSuccess extends ShivaSvCalling with SummaryPipeline {

  @Test(dataProvider = "callers")
  def assertOutputExists(svCaller: SvCaller): Unit = {
    val dir = new File(outputDir, svCaller.svCallerName)
    dir should exist
  }

  addSettingsTest(
    SummaryGroup("shivasvcalling"),
    "sv_callers" :: Nil,
    caller => {
      caller match {
        case l: List[String] =>
          svCallers.foreach(svCaller =>
            require(l.size == svCallers.size && l.contains(svCaller.svCallerName)))
        case _ => throw new IllegalStateException("'sv_callers' should be a List[String]")
      }
    }
  )
}
