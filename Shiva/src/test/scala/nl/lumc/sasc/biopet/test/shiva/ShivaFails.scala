package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.{ TestReference, Samples, PipelineFail }

/**
 * Created by pjvanthof on 19/10/15.
 */
class ShivaNoSamplesTest extends Shiva with PipelineFail {
  override def variantcallers = "haplotypecaller" :: Nil
  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""No Samples found in config""".r)
}

class ShivaNoReferenceTest extends Shiva with PipelineFail {
  override def variantcallers = "haplotypecaller" :: Nil
  override def configs = super.configs ::: Samples.wgs1Config :: Nil
  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: reference_fasta""".r)
}

class ShivaNoOutputDirTest extends Shiva with PipelineFail with TestReference {
  override def variantcallers = "haplotypecaller" :: Nil
  override def outputDirArg = None
  override def configs = super.configs ::: Samples.wgs1Config :: Nil
  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: output_dir""".r)
}

class ShivaWrongBamHeader extends Shiva with PipelineFail with TestReference {
  override def configs = super.configs ::: Samples.wgs1BamWrongHeaderConfig :: Nil
  //TODO: Add log checks
  logMustNotHave("""FunctionEdge - Starting""".r)
}