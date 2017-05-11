package nl.lumc.sasc.biopet.test

import org.testng.annotations.Test

/**
  * Created by pjvanthof on 19/09/15.
  */
trait PipelineFail extends Pipeline {
  @Test(priority = -1) override def exitcode = exitValue should not be 0
}
