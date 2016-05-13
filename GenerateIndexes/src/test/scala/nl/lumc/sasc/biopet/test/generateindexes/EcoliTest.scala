package nl.lumc.sasc.biopet.test.generateindexes

import java.io.File

/**
 * Created by pjvan_thof on 13-5-16.
 */
class EcoliTest extends GenerateIndexes {
  override def referenceConfigs = new File(resourcePath("/E.coli_K-12_MG1655.yml")) :: super.referenceConfigs
}
