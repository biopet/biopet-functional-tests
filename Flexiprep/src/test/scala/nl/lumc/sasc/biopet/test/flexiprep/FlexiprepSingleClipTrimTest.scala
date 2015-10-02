package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

class FlexiprepSingleClipTrimTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=false", "-cv", "skip_trim=false")

  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")

}
