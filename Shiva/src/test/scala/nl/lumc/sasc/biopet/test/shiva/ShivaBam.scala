package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.Samples
import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.shiva.variantcallers.Unifiedgenotyper

/**
 * Created by pjvan_thof on 10/22/15.
 */
class ShivaWgs1BamTest extends ShivaSuccess with BwaMem with TestReference with Unifiedgenotyper {
  override def configs = super.configs ::: Samples.wgs1BamConfig :: Nil
  def samples = Map("wgs1" -> List("lib1"))

  addNotHavingExecutable("bwamem")
}

class ShivaWgs1BamToFastqTest extends ShivaSuccess with BwaMem with TestReference with Unifiedgenotyper {
  override def configs = super.configs ::: Samples.wgs1BamConfig :: Nil
  override def bamToFastq = Some(true)
  def samples = Map("wgs1" -> List("lib1"))

  addExecutable(Executable("bwamem", Some(""".+""".r)))
  addExecutable(Executable("samtofastq", Some(""".+""".r)))
}

class ShivaWgs1BamReplaceReadGroupTest extends ShivaSuccess with BwaMem with TestReference with Unifiedgenotyper {
  override def configs = super.configs ::: Samples.wgs1BamWrongHeaderConfig :: Nil
  override def correctReadgroups = Some(true)
  def samples = Map("wgs1" -> List("lib1"))

  addNotHavingExecutable("bwamem")
  addExecutable(Executable("addorreplacereadgroups", Some(""".+""".r)))
}
