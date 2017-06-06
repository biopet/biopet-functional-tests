package nl.lumc.sasc.biopet.test.downloadgenomes

import nl.lumc.sasc.biopet.test.PipelineSuccess

/**
  * Created by pjvan_thof on 6-6-17.
  */
class ValidateAnnotationsTest extends ValidateAnnotations with PipelineSuccess {
  logMustNotHave("Corrupt annotations files found".r)
}
