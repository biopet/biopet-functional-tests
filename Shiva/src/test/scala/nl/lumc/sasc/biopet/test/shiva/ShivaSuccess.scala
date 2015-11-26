package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import htsjdk.samtools.SamReaderFactory
import nl.lumc.sasc.biopet.test.MultisampleSuccess
import nl.lumc.sasc.biopet.test.SummaryPipeline.Executable
import org.json4s._
import org.testng.annotations.{ DataProvider, Test }

import scala.collection.JavaConversions._

/**
 * Created by pjvan_thof on 9/17/15.
 */
trait ShivaSuccess extends Shiva with MultisampleSuccess {

  if (dbsnpVcfFile.isEmpty && !useBaseRecalibration.contains(false))
    logMustHave("""No Known site found, skipping base recalibration""".r)
  else logMustNotHave("""No Known site found, skipping base recalibration""".r)

  if (!useIndelRealigner.contains(false)) {
    addExecutable(Executable("realignertargetcreator", Some(""".+""".r)))
    addExecutable(Executable("indelrealigner", Some(""".+""".r)))
  } else {
    addNotHavingExecutable("realignertargetcreator")
    addNotHavingExecutable("indelrealigner")
  }

  if (!useBaseRecalibration.contains(false) && dbsnpVcfFile.isDefined) {
    addExecutable(Executable("baserecalibrator", Some(""".+""".r)))
    addExecutable(Executable("printreads", Some(""".+""".r)))
  } else {
    addNotHavingExecutable("baserecalibrator")
    addNotHavingExecutable("printreads")
  }

  samples.foreach {
    case (sampleName, libs) =>
      addSummaryTest(Seq("samples", sampleName, "shiva", "settings"), Seq(
        _ \ "single_sample_variantcalling" shouldBe JBool(singleSampleVariantcalling.getOrElse(false))
      ))
      libs.foreach {
        case lib =>
          addSummaryTest(Seq("samples", sampleName, "libraries", lib, "shiva", "settings"), Seq(
            _ \ "library_variantcalling" shouldBe JBool(libraryVariantcalling.getOrElse(false))
          ))
      }
  }

  addSummaryTest(Seq("shiva", "settings"), Seq(
    _ \ "multisample_variantcalling" shouldBe JBool(multisampleVariantcalling.getOrElse(true)),
    _ \ "sv_calling" shouldBe JBool(svCalling.getOrElse(false)),
    _ \ "annotation" shouldBe JBool(annotation.getOrElse(false)),
    _ \ "reference" shouldBe a[JObject],
    _ \ "regions_of_interest" should not be JNothing,
    _ \ "amplicon_bed" should not be JNothing
  ))

  def minPrecision = 0.95
  def minRecall = 0.95

  def addConcordanceChecks(path: Seq[String], condition: Boolean): Unit = {
    addSummaryTest(path,
      if (condition && referenceVcf.isDefined)
        Seq(
        v => {
          v \ "Overall_Genotype_Concordance" should not be JNothing
          (v \ "Overall_Genotype_Concordance").extract[Double] should be >= (List(minPrecision, minRecall).sum / 2)
        },
        _ \ "Non-Reference_Sensitivity" should not be JNothing,
        _ \ "Non-Reference_Discrepancy" should not be JNothing
      )
      else Seq(_ shouldBe JNothing)
    )
  }

  ("final" :: Shiva.validVariantcallers).foreach {
    case variantcaller =>
      (samples.keySet + "ALL").foreach {
        case sample =>
          addConcordanceChecks(
            Seq("shivavariantcalling", "stats", s"multisample-genotype_concordance-$variantcaller", "genotypeSummary", sample),
            !multisampleVariantcalling.contains(false) && (variantcallers.contains(variantcaller) || variantcaller == "final")
          )
      }

      samples.keySet.foreach {
        case sample =>
          addConcordanceChecks(
            Seq("samples", sample, "shivavariantcalling", "stats", s"$sample-genotype_concordance-$variantcaller", "genotypeSummary", sample),
            singleSampleVariantcalling.contains(true) && (variantcallers.contains(variantcaller) || variantcaller == "final")
          )
          addConcordanceChecks(
            Seq("samples", sample, "shivavariantcalling", "stats", s"$sample-genotype_concordance-$variantcaller", "genotypeSummary", "ALL"),
            singleSampleVariantcalling.contains(true) && (variantcallers.contains(variantcaller) || variantcaller == "final")
          )
      }

      samples.foreach {
        case (sample, libs) => libs.foreach {
          case lib =>
            addConcordanceChecks(
              Seq("samples", sample, "libraries", lib, "shivavariantcalling", "stats", s"$sample-$lib-genotype_concordance-$variantcaller", "genotypeSummary", sample),
              libraryVariantcalling.contains(true) && (variantcallers.contains(variantcaller) || variantcaller == "final")
            )
            addConcordanceChecks(
              Seq("samples", sample, "libraries", lib, "shivavariantcalling", "stats", s"$sample-$lib-genotype_concordance-$variantcaller", "genotypeSummary", "ALL"),
              libraryVariantcalling.contains(true) && (variantcallers.contains(variantcaller) || variantcaller == "final")
            )
        }
      }
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
    val file = new File(dir, s"multisample.$variantcaller.vcf.gz")
    val vcfstats = this.summary \ "shivavariantcalling" \ "stats" \ s"multisample-vcfstats-$variantcaller"
    if (!multisampleVariantcalling.contains(false) && variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      assert(file.exists())
      vcfstats shouldBe a[JObject]
      Shiva.testSamplesVcfFile(file, samples.keySet.toList)
    } else {
      assert(!dir.exists())
      vcfstats shouldBe JNothing
    }
  }

  @Test(dataProvider = "sample-variantcallers", dependsOnGroups = Array("parseSummary"))
  def testSampleVariantcaller(sample: String, variantcaller: String): Unit =
    withClue(s"Variantcaller: $variantcaller, Sample: $sample") {
      val dir = new File(sampleDir(sample), "variantcalling" + File.separator + variantcaller)
      val file = new File(dir, s"$sample.$variantcaller.vcf.gz")
      val vcfstats = this.summary \ "samples" \ sample \ "shivavariantcalling" \ "stats" \ s"$sample-vcfstats-$variantcaller"
      if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
        assert(dir.exists())
        assert(dir.isDirectory)
        assert(file.exists())
        vcfstats shouldBe a[JObject]
        Shiva.testSamplesVcfFile(file, List(sample))
      } else {
        assert(!dir.exists())
        vcfstats shouldBe JNothing
      }
    }

  @Test(dataProvider = "library-variantcallers", dependsOnGroups = Array("parseSummary"))
  def testLibraryVariantcaller(sample: String, lib: String, variantcaller: String): Unit =
    withClue(s"Variantcaller: $variantcaller, Sample: $sample, Lib: $lib") {
      val dir = new File(libraryDir(sample, lib), "variantcalling" + File.separator + variantcaller)
      val file = new File(dir, s"$sample-$lib.$variantcaller.vcf.gz")
      val vcfstats = this.summary \ "samples" \ sample \ "libraries" \ lib \ "shivavariantcalling" \ "stats" \ s"$sample-$lib-vcfstats-$variantcaller"
      if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
        assert(dir.exists())
        assert(dir.isDirectory)
        assert(file.exists())
        vcfstats shouldBe a[JObject]
        Shiva.testSamplesVcfFile(file, List(sample))
      } else {
        assert(!dir.exists())
        vcfstats shouldBe JNothing
      }
    }

  @Test(dependsOnGroups = Array("parseSummary"))
  def testMultisampleVcfFile(): Unit = {
    val file = new File(outputDir, "variantcalling" + File.separator + "multisample.final.vcf.gz")
    val summaryPath = summary \ "shivavariantcalling" \ "files" \ "pipeline" \ "final" \ "path"
    if (!multisampleVariantcalling.contains(false)) {
      summaryPath shouldBe JString(file.getAbsolutePath)
      assert(file.exists())
      Shiva.testSamplesVcfFile(file, samples.keySet.toList)
    } else {
      assert(!file.exists())
      summaryPath shouldBe JNothing
    }
  }

  @Test
  def testMultisampleVariantcallerInfoTag() = testVariantcallerInfoTag(new File(outputDir, "variantcalling" + File.separator + "multisample.final.vcf.gz"))

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSingleSampleVcfFile(sample: String): Unit = withClue(s"Sample: $sample") {
    val file = new File(sampleDir(sample), "variantcalling" + File.separator + s"$sample.final.vcf.gz")
    val summaryPath = summary \ "samples" \ sample \ "shivavariantcalling" \ "files" \ "pipeline" \ "final" \ "path"
    if (singleSampleVariantcalling.contains(true)) {
      summaryPath shouldBe JString(file.getAbsolutePath)
      assert(file.exists())
    } else {
      assert(!file.exists())
      summaryPath shouldBe JNothing
    }
  }

  @Test(dataProvider = "samples")
  def testSampleVariantcallerInfoTag(sample: String) =
    testVariantcallerInfoTag(new File(sampleDir(sample), "variantcalling" + File.separator + s"$sample.final.vcf.gz"))

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibraryVcfFile(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val file = new File(libraryDir(sample, lib), "variantcalling" + File.separator + s"$sample-$lib.final.vcf.gz")
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ "shivavariantcalling" \ "files" \ "pipeline" \ "final" \ "path"
    if (singleSampleVariantcalling.contains(true)) {
      summaryPath shouldBe JString(file.getAbsolutePath)
      assert(file.exists())
    } else {
      assert(!file.exists())
      summaryPath shouldBe JNothing
    }
  }

  @Test(dataProvider = "libraries")
  def testLibraryVariantcallerInfoTag(sample: String, lib: String) =
    testVariantcallerInfoTag(new File(sampleDir(sample), "variantcalling" + File.separator + s"$sample-$lib.final.vcf.gz"))

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testMappingBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ "shiva" \ "files" \ "pipeline" \ "bamFile" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file.getParentFile shouldBe libraryDir(sample, lib)
    file.getName shouldBe s"$sample-$lib.final.bam"
    val replacejob = new File(libraryDir(sample, lib), s".$sample-$lib.final.bam.addorreplacereadgroups.out")
    if (replacejob.exists()) assert(!file.exists())
    else {
      assert(file.exists())

      val reader = SamReaderFactory.makeDefault.open(file)
      val header = reader.getFileHeader
      assert(!header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))
      assert(!header.getProgramRecords.exists(_.getId == "GATK PrintReads"))
      reader.close()
    }
  }

  @Test(dataProvider = "libraries", dependsOnGroups = Array("parseSummary"))
  def testLibraryBam(sample: String, lib: String): Unit = withClue(s"Sample: $sample, Lib: $lib") {
    val summaryPath = summary \ "samples" \ sample \ "libraries" \ lib \ "shiva" \ "files" \ "pipeline" \ "preProcessBam" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file.getParentFile shouldBe libraryDir(sample, lib)
    if (samples(sample).size == 1) {
      assert(file.exists())
      val reader = SamReaderFactory.makeDefault.open(file)
      val header = reader.getFileHeader
      if (!useIndelRealigner.contains(false))
        assert(header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))
      else assert(!header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))

      if (!useBaseRecalibration.contains(false) && dbsnpVcfFile.isDefined)
        assert(header.getProgramRecords.exists(_.getId == "GATK PrintReads"))
      else assert(!header.getProgramRecords.exists(_.getId == "GATK PrintReads"))
      reader.close()
    } else {
      val mappingBam = summary \ "samples" \ sample \ "libraries" \ lib \ "shiva" \ "files" \ "pipeline" \ "bamFile" \ "path"
      if (mappingBam != summaryPath) assert(!file.exists())
    }
  }

  @Test(dataProvider = "samples", dependsOnGroups = Array("parseSummary"))
  def testSampleBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val summaryPath = summary \ "samples" \ sample \ "shiva" \ "files" \ "pipeline" \ "preProcessBam" \ "path"
    summaryPath shouldBe a[JString]
    val file = new File(summaryPath.extract[String])
    file.getParentFile shouldBe sampleDir(sample)
    assert(file.getName.startsWith(s"$sample."))
    assert(file.exists())
    if (samples(sample).size == 1) assert(java.nio.file.Files.isSymbolicLink(file.toPath))

    val reader = SamReaderFactory.makeDefault.open(file)
    val header = reader.getFileHeader

    if (!useIndelRealigner.contains(false))
      assert(header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))
    else assert(!header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))

    if (!useBaseRecalibration.contains(false) && dbsnpVcfFile.isDefined)
      assert(header.getProgramRecords.exists(_.getId == "GATK PrintReads"))
    else assert(!header.getProgramRecords.exists(_.getId == "GATK PrintReads"))

    reader.close()
  }
}
