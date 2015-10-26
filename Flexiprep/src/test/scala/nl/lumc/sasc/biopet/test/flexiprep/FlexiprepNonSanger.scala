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
  override def md5SumInputR1 = "e85a903fad9267e4807a977292adc81f"
}

class FlexiprepPairedNonSangerTest extends FlexiprepPairedDefaultTest with FlexiprepPairedClipTrimSummaryValues {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r1.fq"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "cti_r2.fq"))

  override def inputEncodingR1 = "solexa"
  override def inputEncodingR2 = "solexa"

  /** MD5 checksum of the input file. */
  override def md5SumInputR1 = "e85a903fad9267e4807a977292adc81f"
  override def md5SumInputR2 = Some("b5d471567542333e86533b1961d9501f")
}
