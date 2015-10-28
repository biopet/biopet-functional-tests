package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.{ TestReference, Samples }

/**
 * Created by pjvan_thof on 10/22/15.
 */
class ShivaWgs1BamTest extends ShivaSuccess with TestReference {
  override def configs = super.configs ::: Samples.wgs1BamConfig :: Nil
  override def variantcallers = List("unifiedgenotyper")
  override def aligner = Some("bwa-mem")
  def samples = Map("wgs1" -> List("lib1"))

  addNotExecutable("bwamem")
}

class ShivaWgs1BamToFastqTest extends ShivaSuccess with TestReference {
  override def configs = super.configs ::: Samples.wgs1BamConfig :: Nil
  override def variantcallers = List("unifiedgenotyper")
  override def aligner = Some("bwa-mem")
  override def bamToFastq = Some(true)
  def samples = Map("wgs1" -> List("lib1"))

  addExecutable(Executable("bwamem", Some(""".+""".r)))
  addExecutable(Executable("samtofastq", Some(""".+""".r)))
}

class ShivaWgs1BamReplaceReadGroupTest extends ShivaSuccess with TestReference {
  override def configs = super.configs ::: Samples.wgs1BamWrongHeaderConfig :: Nil
  override def variantcallers = List("unifiedgenotyper")
  override def aligner = Some("bwa-mem")
  override def correctReadgroups = Some(true)
  def samples = Map("wgs1" -> List("lib1"))

  addNotExecutable("bwamem")
  addExecutable(Executable("addorreplacereadgroups", Some(""".+""".r)))
}
