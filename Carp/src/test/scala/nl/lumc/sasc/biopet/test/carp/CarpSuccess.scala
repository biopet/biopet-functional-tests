package nl.lumc.sasc.biopet.test.carp

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMappingSuccess
import org.testng.annotations.{ DataProvider, Test }

/**
 * Created by pjvan_thof on 27-5-16.
 */
trait CarpSuccess extends Carp with MultisampleMappingSuccess {
  //TODO: Add generic tests
  override def samplePreprocessBam(sampleId: String) = new File(sampleDir(sampleId), sampleId + ".filter.bam")

  @Test(dataProvider = "samples")
  def testMacs2(sample: String): Unit = {
    val dir = new File(sampleDir(sample), "macs2" + File.separator + sample)
    new File(dir, s"${sample}_peaks.narrowPeak") should exist
    new File(dir, s"${sample}_peaks.xls") should exist
    new File(dir, s"${sample}_summits.bed") should exist
  }

  @DataProvider(name = "controls")
  def controlProvider = {
    controls.toArray.map { case (sample, control) => Array(sample, control) }
  }

  @Test(dataProvider = "controls")
  def testMacs2Controls(sample: String, control: String): Unit = {
    val dir = new File(sampleDir(sample), "macs2" + File.separator + s"${sample}_VS_$control")
    new File(dir, s"${sample}_VS_${control}_peaks.narrowPeak") should exist
    new File(dir, s"${sample}_VS_${control}_peaks.xls") should exist
    new File(dir, s"${sample}_VS_${control}_summits.bed") should exist
  }
}
