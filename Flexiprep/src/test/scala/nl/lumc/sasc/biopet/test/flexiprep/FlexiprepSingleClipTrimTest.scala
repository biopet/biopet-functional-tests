package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

class FlexiprepSingleClipTrimTest extends FlexiprepRunSingle {

  @Test def testOutputR1() = {
    val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
    assert(calcMd5Unzipped(outputR1) == "5b7896e489a5aeb3d30cb11ea15a7be3")
  }
}
