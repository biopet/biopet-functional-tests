package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.test.PipelineFail

class FlexiprepNoR1Test extends FlexiprepRun with PipelineFail {
  override def args = super.args ++ Seq("-run")
}
