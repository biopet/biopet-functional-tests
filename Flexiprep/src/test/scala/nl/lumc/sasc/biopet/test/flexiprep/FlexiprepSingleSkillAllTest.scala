package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test

class FlexiprepSingleSkillAllTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=true")

}

