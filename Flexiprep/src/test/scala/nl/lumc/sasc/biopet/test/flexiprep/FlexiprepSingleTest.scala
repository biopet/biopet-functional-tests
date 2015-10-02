package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test

class FlexiprepSingleTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=true")

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(!outputR1.exists)
  }
}

