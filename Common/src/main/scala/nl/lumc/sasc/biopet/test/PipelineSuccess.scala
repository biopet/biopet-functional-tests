package nl.lumc.sasc.biopet.test

/**
 * General trait for pipeline that should succeed
 */
trait PipelineSuccess extends Pipeline {
  logMustNotHave("""Script failed with \d+ total jobs""".r)
  logMustHave("""Script completed successfully with \d+ total jobs""".r)
  logMustNotHave(""" - Input file does not exist:""".r)
  logMustNotHave("""DrmaaJobRunner - Unable to determine status of job id""".r)
}
