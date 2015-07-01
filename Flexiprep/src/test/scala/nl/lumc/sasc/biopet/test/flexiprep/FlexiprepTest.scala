package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 5/26/15.
 */
class FlexiprepTest extends Pipeline {

  def pipelineName = "flexiprep"

  def args = Seq("-R1", "bla.fq.gz", "-cv", "output_dir=" + outputDir, "-run")

  @Test(priority = -1) def exitcode = exitValue shouldBe 0
  @Test def outputDirExist = assert(outputDir.exists())
  @Test def logFileExist = assert(new File(outputDir, "run.log").exists())
}
