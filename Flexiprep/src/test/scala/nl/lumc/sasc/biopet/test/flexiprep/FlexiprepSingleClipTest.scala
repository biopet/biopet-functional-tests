package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

class FlexiprepSingleClipTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=false", "-cv", "skip_trim=true")

  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")

}

