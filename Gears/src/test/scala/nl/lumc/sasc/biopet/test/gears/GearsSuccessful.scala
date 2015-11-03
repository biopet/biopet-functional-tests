package nl.lumc.sasc.biopet.test.gears

import java.io.File

import nl.lumc.sasc.biopet.test.SummaryPipeline

/**
 * Created by pjvanthof on 27/10/15.
 */
trait GearsSuccessful extends Gears with SummaryPipeline {

  def summaryFile = new File(outputDir, s"${sampleId.getOrElse("sampleName_unknown")}.gears.summary.json")

}
