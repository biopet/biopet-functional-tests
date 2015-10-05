package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvan_thof on 10/2/15.
 */
/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepSingle extends FlexiprepSuccessful {

  /** Input file of this run. */
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath))

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"
}

class FlexiprepSingleClipTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(true)
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")
}

class FlexiprepSingleClipTrimTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")
}

class FlexiprepSingleSkipAllTest extends FlexiprepSingle {
  override def skipClip = Some(false)
  override def skipTrim = Some(false)
}

class FlexiprepSingleTrimTest extends FlexiprepSingle {
  override def skipClip = Some(true)
  override def skipTrim = Some(false)
  override def md5SumOutputR1 = Some("5001a539ca3cc3312835466bdb37b3d8")
}

class FlexiprepSingleDefaultTest extends FlexiprepSingle {
  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")
}

class FlexiprepSingleRemoveOutputTest extends FlexiprepSingleClipTrimTest {
  override def keepQcFastqFiles = false
}

class FlexiprepSingleUnzippedTest extends FlexiprepSingleClipTrimTest {
  /** Input file of this run. */
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq").getAbsolutePath))

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "b6f564f7496039dfe4e4e9794d191af2"
}