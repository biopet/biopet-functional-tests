package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.samples.{Wgs1WrongBam, Wgs1}
import nl.lumc.sasc.biopet.test.shiva.variantcallers.Haplotypecaller
import nl.lumc.sasc.biopet.test.PipelineFail
import nl.lumc.sasc.biopet.test.Pipeline._

/**
  * Created by pjvanthof on 19/10/15.
  */
class ShivaNoSamplesTest
    extends Shiva
    with PipelineFail
    with BwaMem
    with TestReference
    with Haplotypecaller {
  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""No Samples found in config""".r)
}

class ShivaNoReferenceTest extends Shiva with PipelineFail with Haplotypecaller with Wgs1 {
  def referenceSpecies = None
  def referenceName = None
  def referenceFasta = None
  def aligner = Some("bwa-mem")
  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: reference_fasta""".r)
}

class ShivaNoOutputDirTest
    extends Shiva
    with PipelineFail
    with BwaMem
    with TestReference
    with Haplotypecaller
    with Wgs1 {
  override def outputDirArg = None
  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: output_dir""".r)
}

class ShivaWrongBamHeaderTest
    extends Shiva
    with PipelineFail
    with BwaMem
    with TestReference
    with Haplotypecaller
    with Wgs1WrongBam {
  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Sample readgroup and/or library of input bamfile is not correct,""".r)
}

class ShivaWrongMd5Test
    extends Shiva
    with PipelineFail
    with BwaMem
    with TestReference
    with Haplotypecaller
    with Wgs1 {
  override def args =
    super.args ++ cmdConfig("samples:wgs1:libraries:lib1:R1_md5", "this_should_not_match_md5")
  logMustHave("""FunctionEdge - Starting""".r)
  logMustHave("""md5sum is not as expected, aborting pipeline""".r)
}
