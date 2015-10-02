package nl.lumc.sasc.biopet.test.flexiprep

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._

class FlexiprepSingleTrimTest extends FlexiprepRunSingle {

  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=false")

  override def md5SumOutputR1 = Some("5001a539ca3cc3312835466bdb37b3d8")

}

