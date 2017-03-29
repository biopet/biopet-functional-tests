package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvanthof on 04/10/15.
 */
class FlexiprepSingleNonSangerTest extends FlexiprepSingleDefaultTest {
  /** Input file of this run. */
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r1.fq"))

  override def inputEncodingR1 = "solexa"

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "332762ce0ea39a4bcb03de719d2229f2"

}

class FlexiprepPairedNonSangerTest extends FlexiprepPairedDefaultTest {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r1.fq"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r2.fq"))

  override def inputEncodingR1 = "solexa"
  override def inputEncodingR2 = "solexa"

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "332762ce0ea39a4bcb03de719d2229f2"
  override def md5SumInputR2 = Some("6519425e6191e136a4e29f4642ac0b84")

}
