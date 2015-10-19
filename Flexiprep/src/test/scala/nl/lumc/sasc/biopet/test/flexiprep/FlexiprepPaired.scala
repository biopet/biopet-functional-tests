package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvan_thof on 10/2/15.
 */
/** Trait for Flexiprep runs with paired-end inputs. */
trait FlexiprepPaired extends FlexiprepSingle {

  /** Input read pair 2 for this run. */
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))

  /** MD5 checksum of input read pair 2. */
  override def md5SumInputR2 = Some("1560a4cdc87cc8c4b6701e1253d41f93")
}

class FlexiprepPairedClipTest extends FlexiprepPaired {
  override def skipClip = Some(false)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")
  override def md5SumOutputR2 = Some("706f9a97239d1e2110d7b48605559d22")
}

class FlexiprepPairedClipTrimTest extends FlexiprepPaired {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("b88c68ac883086d558a76cd2fa9252fc")
  override def md5SumOutputR2 = Some("2acfae7d3ead4c6054786a1b5eef2b17")
}

class FlexiprepPairedSkipAllTest extends FlexiprepPaired {
  override def skipClip = Some(true)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("b6f564f7496039dfe4e4e9794d191af2")
  override def md5SumOutputR2 = Some("707e26b2fb5a2c999783a2830931f952")
}

class FlexiprepPairedTrimTest extends FlexiprepPaired {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("1a468148f163dfc8cd46e479706efadd")
  override def md5SumOutputR2 = Some("13cc49a8e8203a634e7bbe07723f13b7")
}

class FlexiprepPairedDefaultTest extends FlexiprepPaired {
  override def md5SumOutputR1 = Some("b88c68ac883086d558a76cd2fa9252fc")
  override def md5SumOutputR2 = Some("2acfae7d3ead4c6054786a1b5eef2b17")
}

class FlexiprepPairedRemoveOutputTest extends FlexiprepPairedClipTrimTest {
  override def keepQcFastqFiles = Some(false)
}

class FlexiprepPairedKeepOutputTest extends FlexiprepPairedClipTrimTest {
  override def keepQcFastqFiles = Some(true)
}

class FlexiprepPairedUnzippedTest extends FlexiprepPairedClipTrimTest {
  /** Input file of this run. */
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq").getAbsolutePath))

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "b6f564f7496039dfe4e4e9794d191af2"

  /** Input read pair 2 for this run. */
  override def r2 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq").getAbsolutePath))

  /** MD5 checksum of input read pair 2. */
  override def md5SumInputR2 = Some("707e26b2fb5a2c999783a2830931f952")

}