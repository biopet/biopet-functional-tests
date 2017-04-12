package nl.lumc.sasc.biopet.test.shiva.svcalling

import java.io.File

import nl.lumc.sasc.biopet.test.{ SummaryGroup, SummaryPipeline }
import nl.lumc.sasc.biopet.test.shiva.svcallers.{ Breakdancer, Clever, Delly, SvCaller }
import org.testng.annotations.Test

trait ShivaSvCallingSuccess extends ShivaSvCalling with SummaryPipeline {

  @Test(dataProvider = "callers")
  def assertOutputExists(svCaller: SvCaller): Unit = {
    val dir = new File(outputDir, svCaller.svCallerName)
    dir should exist
  }

  addSettingsTest(SummaryGroup("shivasvcalling"), "sv_callers" :: Nil, caller => {
    caller match {
      case l: List[String] =>
        if (svCallers.exists(_.isInstanceOf[Breakdancer])) require(l.contains("breakdancer"))
        else require(!l.contains("breakdancer"))
        if (svCallers.exists(_.isInstanceOf[Clever])) require(l.contains("clever"))
        else require(!l.contains("clever"))
        if (svCallers.exists(_.isInstanceOf[Delly])) require(l.contains("delly"))
        else require(!l.contains("delly"))
      case _ => throw new IllegalStateException("'sv_callers' should be a List[String]")
    }
  })
}
