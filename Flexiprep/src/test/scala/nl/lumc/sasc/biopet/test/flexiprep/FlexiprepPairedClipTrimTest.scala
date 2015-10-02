package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

class FlexiprepPairedClipTrimTest extends FlexiprepRunSingle {

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "b88c68ac883086d558a76cd2fa9252fc")
  }

  @Test def testOutputR2() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "2acfae7d3ead4c6054786a1b5eef2b17")
  }
}
