package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.test.PipelineFail

trait FlexiprepRunFails extends FlexiprepRun with PipelineFail

class FlexiprepNoR1Test extends FlexiprepRunFails {
  override def args = super.args ++ Seq("-run")
}
