package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test

class FlexiprepRunPaired extends FlexiprepRunSingle {

   @Test def testOutputR1() = {
     val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
     assert(calcMd5Unzipped(outputR1) == "b6f564f7496039dfe4e4e9794d191af2")
   }

   @Test def testOutputR2() = {
     val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
     assert(calcMd5Unzipped(outputR1) == "707e26b2fb5a2c999783a2830931f952")
   }
 }
