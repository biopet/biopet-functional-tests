package nl.lumc.sasc.biopet.test.flexiprep

import java.io.File

import org.testng.annotations.Test
import nl.lumc.sasc.biopet.test.utils._
import org.json4s._

class FlexiprepPairedTest extends FlexiprepRunSingle {

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR1() = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "output_R1"
    (summaryFile \ "path") shouldBe JString
    (summaryFile \ "md5") shouldBe JString
    val file = new File((summaryFile \ "path").extract[String])
    val md5 = calcMd5Unzipped(file)

    md5 shouldBe "b6f564f7496039dfe4e4e9794d191af2"
    md5 shouldBe (summaryFile \ "md5").extract[String]
  }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testOutputR2() = {
    val summaryFile = summary \ "samples" \ sampleId \ "libraries" \ libId \ "flexiprep" \ "files" \ "output_R2"
    (summaryFile \ "path") shouldBe JString
    (summaryFile \ "md5") shouldBe JString
    val file = new File((summaryFile \ "path").extract[String])
    val md5 = calcMd5Unzipped(file)

    md5 shouldBe "707e26b2fb5a2c999783a2830931f952"
    md5 shouldBe (summaryFile \ "md5").extract[String]
  }
}
