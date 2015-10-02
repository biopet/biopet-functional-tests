package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

class FlexiprepPairedClipTest extends FlexiprepRunSingle {

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "037aa58f60372c11037bef9ac157777e")
  }

  @Test def testOutputR2() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "706f9a97239d1e2110d7b48605559d22")
  }
}
