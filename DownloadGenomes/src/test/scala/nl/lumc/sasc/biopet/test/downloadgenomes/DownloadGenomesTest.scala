package nl.lumc.sasc.biopet.test.downloadgenomes

import java.io.File

/**
 * Created by pjvan_thof on 13-5-16.
 */
class EcoliTest extends DownloadGenomes {
  override def referenceConfigs = new File(resourcePath("/E.coli_K-12_MG1655.yml")) :: super.referenceConfigs
}

class CelegansTest extends DownloadGenomes {
  override def referenceConfigs = new File(resourcePath("/C.elegans_WBcel235.yml")) :: super.referenceConfigs
}
