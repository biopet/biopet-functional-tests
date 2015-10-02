package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

class FlexiprepPairedClipTrimTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=false", "-cv", "skip_trim=false")

  override def md5SumOutputR1 = Some("b88c68ac883086d558a76cd2fa9252fc")
  override def md5SumOutputR2 = Some("2acfae7d3ead4c6054786a1b5eef2b17")

}
