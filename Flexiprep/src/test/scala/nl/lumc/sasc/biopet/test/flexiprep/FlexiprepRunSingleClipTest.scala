package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test

class FlexiprepRunSingleClipTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_trim=true")

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "037aa58f60372c11037bef9ac157777e")
  }
}

