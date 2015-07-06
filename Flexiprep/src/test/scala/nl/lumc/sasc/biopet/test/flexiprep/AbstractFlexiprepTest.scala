package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 5/26/15.
 */
abstract class AbstractFlexiprepTest extends Pipeline {

  def pipelineName = "flexiprep"

  def sampleId = "sampleName"

  def libId = "libName"

  def args = Seq("-R1", "bla.fq.gz", "-cv", "output_dir=" + outputDir, "-run")
}
