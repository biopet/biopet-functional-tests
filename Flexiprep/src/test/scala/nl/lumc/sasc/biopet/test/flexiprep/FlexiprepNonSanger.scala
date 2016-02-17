package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet

/**
 * Created by pjvanthof on 04/10/15.
 */
class FlexiprepSingleNonSangerTest extends FlexiprepSingleDefaultTest with FlexiprepSingleClipTrimSummaryValues {
  /** Input file of this run. */
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r1.fq"))

  override def inputEncodingR1 = "solexa"

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "332762ce0ea39a4bcb03de719d2229f2"

  override def md5SumOutputR1 = Some("d082ebf2ded2fdae481b951f4f6e32ab")

}

class FlexiprepPairedNonSangerTest extends FlexiprepPairedDefaultTest with FlexiprepPairedClipTrimSummaryValues {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r1.fq"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r2.fq"))

  override def inputEncodingR1 = "solexa"
  override def inputEncodingR2 = "solexa"

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "332762ce0ea39a4bcb03de719d2229f2"
  override def md5SumInputR2 = Some("6519425e6191e136a4e29f4642ac0b84")

  override def md5SumOutputR1 = Some("1d7f59553ea9fcd073acb5cbfc22156f")
  override def md5SumOutputR2 = Some("8ea8ceef563c63b122be8ef9a8d10d2")
}
