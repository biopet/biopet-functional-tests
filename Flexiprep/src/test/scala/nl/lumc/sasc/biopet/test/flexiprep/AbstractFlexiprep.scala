package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.SummaryPipeline

/**
 * Created by pjvan_thof on 5/26/15.
 */
abstract class AbstractFlexiprep extends SummaryPipeline {

  def pipelineName = "flexiprep"

  def sampleId = "sampleName"

  def libId = "libName"

  def summaryFile = new File(outputDir, s"${sampleId}-${libId}.qc.summary.json")

  def args = Seq("-sample", sampleId, "-library", libId, "-cv", "output_dir=" + outputDir, "-run")
}
