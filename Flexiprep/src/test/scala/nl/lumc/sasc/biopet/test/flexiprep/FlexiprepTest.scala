package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.test.{ Pipeline }
import org.testng.annotations.{ AfterClass, Test }

/**
 * Created by pjvan_thof on 5/26/15.
 */
class FlexiprepTest extends Pipeline {

  def pipelineName = "flexiprep"

  def args = Seq("-R1", "bla.fq.gz", "-cv", "output_dir=" + outputDir)

  @Test(priority = -1) def exitcodeTest = exitValue shouldBe 0
  @Test def createOutputDir = assert(outputDir.exists())
}
