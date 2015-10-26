package nl.lumc.sasc.biopet.test.shiva

import nl.lumc.sasc.biopet.test.{ TestReference, Samples, PipelineFail }
import nl.lumc.sasc.biopet.test.Pipeline._

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

class ShivaWrongMd5Test extends Shiva with PipelineFail {
  override def variantcallers = "haplotypecaller" :: Nil
  override def configs = super.configs ::: Samples.wgs1Config :: Nil
  override def args = super.args ++ cmdConfig("samples:wgs1:libraries:lib1:R1_md5", "this_should_not_match_md5")
  logMustHave("""FunctionEdge - Starting""".r)
  //TODO: Add log checks
}
