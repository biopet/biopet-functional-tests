package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleSuccess
import nl.lumc.sasc.biopet.test.references.Reference

/**
 * Created by pjvanthof on 05/11/15.
 */
trait GentrapSuccess extends Gentrap with MultisampleSuccess { this: GentrapAnnotations with Reference with ExpressionMeasures =>
  logMustNotHave("""Script failed with \d+ total jobs""".r)
  logMustHave("""Script completed successfully with \d+ total jobs""".r)
}
