package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test

class FlexiprepRunPairedTrim extends FlexiprepRunSingle {

   @Test def testOutputR1() = {
     val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
     assert(calcMd5Unzipped(outputR1) == "1a468148f163dfc8cd46e479706efadd")
   }

   @Test def testOutputR2() = {
     val outputR1 = getOutputFile("ct_r1.qc.fq.gz")
     assert(calcMd5Unzipped(outputR1) == "13cc49a8e8203a634e7bbe07723f13b7")
   }
 }
