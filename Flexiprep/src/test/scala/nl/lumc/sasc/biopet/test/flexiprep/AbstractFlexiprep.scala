package nl.lumc.sasc.biopet.test.flexiprep

import nl.lumc.sasc.biopet.test.Pipeline

/**
 * Created by pjvan_thof on 5/26/15.
 */
abstract class AbstractFlexiprep extends Pipeline {

  def pipelineName = "flexiprep"

  def sampleId = "sampleName"

  def libId = "libName"

  override def args = super.args ++ Seq("-sample", sampleId, "-library", libId, "-cv", "output_dir=" + outputDir, "-run")
}
