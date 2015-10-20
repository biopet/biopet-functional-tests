package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleSuccess
import nl.lumc.sasc.biopet.test.shiva.variantcalling.VariantcallersExecutables
import org.json4s._
import org.testng.annotations.{ DataProvider, Test }

/**
 * Created by pjvan_thof on 9/17/15.
 */
trait ShivaSuccess extends Shiva with MultisampleSuccess with VariantcallersExecutables {

  if (dbsnpVcfFile.isEmpty && !useBaseRecalibration.contains(false))
    logMustHave("""No Known site found, skipping base recalibration""".r)
  else logMustNotHave("""No Known site found, skipping base recalibration""".r)

  if (!useIndelRealigner.contains(false)) {
    addExecutable(Executable("realignertargetcreator", Some( """.+""".r)))
    addExecutable(Executable("indelrealigner", Some(""".+""".r)))
  } else {
    addNotExecutable("realignertargetcreator")
    addNotExecutable("indelrealigner")
  }

  if (!useBaseRecalibration.contains(false) && dbsnpVcfFile.isDefined) {
    addExecutable(Executable("baserecalibrator", Some( """.+""".r)))
    addExecutable(Executable("printreads", Some(""".+""".r)))
  } else {
    addNotExecutable("baserecalibrator")
    addNotExecutable("printreads")
  }

  @DataProvider(name = "variantcallers")
  def variantcallerProvider = Shiva.validVariantcallers.map(Array(_)).toArray

  @DataProvider(name = "sample-variantcallers")
  def sampleVariantcallers = {
    (for (sample <- samples.keys; variantcaller <- Shiva.validVariantcallers) yield Array(sample, variantcaller)).toArray
  }

  @DataProvider(name = "library-variantcallers")
  def libraryVariantcallers = {
    (for ((sample, libs) <- samples; lib <- libs; variantcaller <- Shiva.validVariantcallers) yield Array(sample, lib, variantcaller)).toArray
  }

  @Test(dataProvider = "variantcallers", dependsOnGroups = Array("parseSummary"))
  def testVariantcaller(variantcaller: String): Unit = withClue(s"Variantcaller: $variantcaller") {
    val dir = new File(outputDir, "variantcalling" + File.separator + variantcaller)
    val vcfstats = this.summary \ "shivavariantcalling" \ "stats" \ s"multisample-vcfstats-$variantcaller"
    if (!multisampleVariantcalling.contains(false) && variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      vcfstats shouldBe a[JObject]
    } else {
      assert(!dir.exists())
      vcfstats shouldBe JNothing
    }
  }

  @Test(dataProvider = "sample-variantcallers", dependsOnGroups = Array("parseSummary"))
  def testSampleVariantcaller(sample: String, variantcaller: String): Unit =
    withClue(s"Variantcaller: $variantcaller, Sample: $sample") {
      val dir = new File(sampleDir(sample), "variantcalling" + File.separator + variantcaller)
      val vcfstats = this.summary \ "samples" \ sample \ "shivavariantcalling" \ "stats" \ s"$sample-vcfstats-$variantcaller"
      if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
        assert(dir.exists())
        assert(dir.isDirectory)
        vcfstats shouldBe a[JObject]
      } else {
        assert(!dir.exists())
        vcfstats shouldBe JNothing
      }
    }

  @Test(dataProvider = "library-variantcallers", dependsOnGroups = Array("parseSummary"))
  def testLibraryVariantcaller(sample: String, lib: String, variantcaller: String): Unit =
    withClue(s"Variantcaller: $variantcaller, Sample: $sample, Lib: $lib") {
      val dir = new File(libraryDir(sample, lib), "variantcalling" + File.separator + variantcaller)
      val vcfstats = this.summary \ "samples" \ sample \ "libraries" \ lib \ "shivavariantcalling" \ "stats" \ s"$sample-$lib-vcfstats-$variantcaller"
      if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
        assert(dir.exists())
        assert(dir.isDirectory)
        vcfstats shouldBe a[JObject]
      } else {
        assert(!dir.exists())
        vcfstats shouldBe JNothing
      }
    }

  @Test(dataProvider = "libraries")
  def testLibraryBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val file = new File(libraryDir(sample, lib), s"$sample-$lib.final.bam")
    assert(file.exists())
  }

  @Test(dataProvider = "samples")
  def testSampleBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val file = new File(sampleDir(sample), s"$sample.final.bam")
    assert(file.exists())
  }
}
