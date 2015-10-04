package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvan_thof on 10/2/15.
 */
/** Trait for Flexiprep runs with single-end inputs. */
trait FlexiprepRunSingle extends SuccessfulFlexiprep {

  /** Input file of this run. */
  override def r1 = Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath))

  /** MD5 checksum of the input file. */
  def md5SumInputR1 = "8245507d70154d7921cd1bcce1ea344b"
}

class FlexiprepSingleClipTest extends FlexiprepRunSingle {
  override def args = super.args ++ Seq("-cv", "skip_clip=false", "-cv", "skip_trim=true")
  override def md5SumOutputR1 = Some("037aa58f60372c11037bef9ac157777e")
}

class FlexiprepSingleClipTrimTest extends FlexiprepRunSingle {
  override def args = super.args ++ Seq("-cv", "skip_clip=false", "-cv", "skip_trim=false")
  override def md5SumOutputR1 = Some("5b7896e489a5aeb3d30cb11ea15a7be3")
}

class FlexiprepSingleSkillAllTest extends FlexiprepRunSingle {
  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=true")
}

class FlexiprepSingleTrimTest extends FlexiprepRunSingle {
  override def args = super.args ++ Seq("-cv", "skip_clip=true", "-cv", "skip_trim=false")
  override def md5SumOutputR1 = Some("5001a539ca3cc3312835466bdb37b3d8")
}

class FlexiprepSingleRemoveOutput extends FlexiprepSingleClipTrimTest {
  override def keepQcFastqFiles = false
}