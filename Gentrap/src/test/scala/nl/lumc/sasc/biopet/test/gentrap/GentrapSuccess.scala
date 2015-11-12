package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleSuccess

/**
 * Created by pjvanthof on 05/11/15.
 */
trait GentrapSuccess extends Gentrap with MultisampleSuccess { this: GentrapAnnotations =>
  logMustNotHave("""Script failed with \d+ total jobs""".r)
  logMustHave("""Script completed successfully with \d+ total jobs""".r)

  override def sampleDir(sampleId: String) = new File(outputDir, s"samples_$sampleId")
}
