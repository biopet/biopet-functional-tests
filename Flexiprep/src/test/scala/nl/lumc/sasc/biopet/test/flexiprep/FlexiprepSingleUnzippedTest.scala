package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

class FlexiprepSingleUnzippedTest extends FlexiprepSingleClipTrimTest {

  /** Input file of this run. */
  override def r1 =
    Some(new File(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq").getAbsolutePath))

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "b6f564f7496039dfe4e4e9794d191af2"
}
