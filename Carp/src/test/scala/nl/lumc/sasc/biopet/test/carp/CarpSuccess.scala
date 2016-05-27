package nl.lumc.sasc.biopet.test.carp

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMappingSuccess

/**
 * Created by pjvan_thof on 27-5-16.
 */
trait CarpSuccess extends Carp with MultisampleMappingSuccess {
  //TODO: Add generic tests
  override def samplePreprocessBam(sampleId: String) = new File(sampleDir(sampleId), sampleId + ".filter.bam")
}
