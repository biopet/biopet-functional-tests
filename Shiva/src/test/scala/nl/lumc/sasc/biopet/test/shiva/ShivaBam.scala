package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.SummaryPipeline.Executable
import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.samples.{ Wgs1WrongBam, Wgs1Bam }
import nl.lumc.sasc.biopet.test.shiva.variantcallers.Unifiedgenotyper

/**
 * Created by pjvan_thof on 10/22/15.
 */
class ShivaWgs1BamTest extends ShivaSuccess with BwaMem with TestReference with Unifiedgenotyper with Wgs1Bam {
  addNotHavingExecutable("bwamem")
  def paired = true
  override def testLibraryBam(sampleid: String, libId: String) = {}
}

class ShivaWgs1BamToFastqTest extends ShivaSuccess with BwaMem with TestReference with Unifiedgenotyper with Wgs1Bam {
  override def bamToFastq = Some(true)
  def paired = true
  addExecutable(Executable("bwamem", Some(""".+""".r)))
  addExecutable(Executable("samtofastq", Some(""".+""".r)))
}

class ShivaWgs1BamReplaceReadGroupTest extends ShivaSuccess with BwaMem with TestReference
  with Unifiedgenotyper with Wgs1WrongBam {
  override def correctReadgroups = Some(true)
  def paired = true
  /** This bam file should only in this conditions not exist, disabled test */
  override def testLibraryBam(sampleid: String, libId: String) = {}

  addNotHavingExecutable("bwamem")
  addExecutable(Executable("addorreplacereadgroups", Some(""".+""".r)))
}
