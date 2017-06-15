package nl.lumc.sasc.biopet.test.shiva

import java.io.File

import htsjdk.samtools.SamReaderFactory
import nl.lumc.sasc.biopet.test.{Executable, MultisampleMappingSuccess, SummaryGroup}
import org.testng.annotations.{DataProvider, Test}
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb.Implicts._

import scala.collection.JavaConversions._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by pjvan_thof on 9/17/15.
  */
trait ShivaSuccess extends Shiva with MultisampleMappingSuccess {

  val shivaGroup = SummaryGroup("shiva")
  val shivavariantcallingGroup = SummaryGroup("shivavariantcalling")

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
    if (usePrintReads.contains(false)) addNotHavingExecutable("printreads")
    else addExecutable(Executable("printreads", Some(""".+""".r)))
  } else {
    addNotHavingExecutable("baserecalibrator")
    addNotHavingExecutable("printreads")
  }

  samples.foreach {
    case (sampleName, libs) =>
      val sampleGroup = shivaGroup.copy(sample = sampleName)
      addSettingsTest(sampleGroup,
                      "single_sample_variantcalling" :: Nil,
                      _ shouldBe singleSampleVariantcalling.getOrElse(false))
      libs.foreach { lib =>
        val libraryGroup = sampleGroup.copy(library = lib)
        addSettingsTest(libraryGroup,
                        "library_variantcalling" :: Nil,
                        _ shouldBe libraryVariantcalling.getOrElse(false))
      }
  }

  addSettingsTest(shivaGroup,
                  "multisample_variantcalling" :: Nil,
                  _ shouldBe multisampleVariantcalling.getOrElse(true))
  addSettingsTest(shivaGroup, "sv_calling" :: Nil, _ shouldBe svCalling.getOrElse(false))
  addSettingsTest(shivaGroup, "annotation" :: Nil, _ shouldBe annotation.getOrElse(false))
  addSettingsTest(shivaGroup, "reference" :: Nil, _ shouldBe a[Map[_, _]])

  def minPrecision = 0.95
  def minRecall = 0.95

  override def samplePreprocessBam(sampleId: String) =
    new File(
      super.samplePreprocessBam(sampleId).getAbsolutePath.stripSuffix(".bam") +
        (if (useIndelRealigner.getOrElse(true)) ".realign.bam" else ".bam"))

  override def libraryPreprecoessBam(sampleId: String, libId: String) =
    new File(
      super.libraryPreprecoessBam(sampleId, libId).getAbsolutePath.stripSuffix(".bam") +
        (if (useBaseRecalibration
               .getOrElse(true) && dbsnpVcfFile.isDefined && !usePrintReads.contains(false))
           ".baserecal.bam"
         else ".bam"))

  def addConcordanceChecks(group: SummaryGroup, sample: String, condition: Boolean): Unit = {
    addStatsTest(
      group,
      "genotypeSummary" :: sample :: "Overall_Genotype_Concordance" :: Nil,
      shouldExist = condition && referenceVcf.isDefined,
      test = _ shouldBe >=(List(minPrecision, minRecall).sum / 2)
    )
  }

  ("final" :: Shiva.validVariantcallers).foreach { variantcaller =>
    (samples.keySet + "ALL").foreach { sample =>
      val group =
        shivavariantcallingGroup.copy(module = s"multisample-genotype_concordance-$variantcaller")
      val condition = !multisampleVariantcalling.contains(false) && (variantcallers.contains(
        variantcaller) || variantcaller == "final")
      addConcordanceChecks(group, sample, condition)
    }

    samples.keySet.foreach { sample =>
      val group =
        shivavariantcallingGroup.copy(module = s"$sample-genotype_concordance-$variantcaller",
                                      sample = sample)
      val condition = singleSampleVariantcalling.contains(true) && (variantcallers.contains(
        variantcaller) || variantcaller == "final")
      addConcordanceChecks(group, sample, condition)
      addConcordanceChecks(group, "ALL", condition)

    }

    samples.foreach {
      case (sample, libs) =>
        libs.foreach { lib =>
          val group =
            shivavariantcallingGroup.copy(module =
                                            s"$sample-$lib-genotype_concordance-$variantcaller",
                                          sample = sample,
                                          library = lib)
          val condition = libraryVariantcalling.contains(true) && (variantcallers.contains(
            variantcaller) || variantcaller == "final")
          addConcordanceChecks(group, sample, condition)
          addConcordanceChecks(group, "ALL", condition)
        }
    }
  }

  @DataProvider(name = "variantcallers")
  def variantcallerProvider: Array[Array[String]] = Shiva.validVariantcallers.map(Array(_)).toArray

  @DataProvider(name = "sample-variantcallers")
  def sampleVariantcallers: Array[Array[String]] = {
    (for (sample <- samples.keys; variantcaller <- Shiva.validVariantcallers)
      yield Array(sample, variantcaller)).toArray
  }

  @DataProvider(name = "library-variantcallers")
  def libraryVariantcallers: Array[Array[String]] = {
    (for ((sample, libs) <- samples; lib <- libs; variantcaller <- Shiva.validVariantcallers)
      yield Array(sample, lib, variantcaller)).toArray
  }

  @Test(dataProvider = "variantcallers", dependsOnGroups = Array("summary"))
  def testVariantcaller(variantcaller: String): Unit = withClue(s"Variantcaller: $variantcaller") {
    val dir = new File(outputDir, "variantcalling" + File.separator + variantcaller)
    val file = new File(dir, s"multisample.$variantcaller.vcf.gz")
    val vcfstats = Await.result(
      summaryDb.getStat(runId, "shivavariantcalling", s"multisample-vcfstats-$variantcaller"),
      Duration.Inf)

    if (!multisampleVariantcalling.contains(false) && variantcallers.contains(variantcaller)) {
      assert(dir.exists())
      assert(dir.isDirectory)
      assert(file.exists())
      assert(vcfstats.isDefined, s"multisample-vcfstats-$variantcaller not found in summary")
      Shiva.testSamplesVcfFile(file, samples.keySet.toList)
    } else {
      assert(!dir.exists())
      assert(vcfstats.isEmpty,
             s"multisample-vcfstats-$variantcaller found in summary but should not exist")
    }
  }

  @Test(dataProvider = "sample-variantcallers", dependsOnGroups = Array("summary"))
  def testSampleVariantcaller(sample: String, variantcaller: String): Unit =
    withClue(s"Variantcaller: $variantcaller, Sample: $sample") {
      val dir = new File(sampleDir(sample), "variantcalling" + File.separator + variantcaller)
      val file = new File(dir, s"$sample.$variantcaller.vcf.gz")
      val vcfstats = Await.result(summaryDb.getStat(runId,
                                                    "shivavariantcalling",
                                                    s"$sample-vcfstats-$variantcaller",
                                                    sample),
                                  Duration.Inf)
      if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
        assert(dir.exists())
        assert(dir.isDirectory)
        assert(file.exists())
        assert(vcfstats.isDefined, s"multisample-vcfstats-$variantcaller not found in summary")
        Shiva.testSamplesVcfFile(file, List(sample))
      } else {
        assert(!dir.exists())
        assert(vcfstats.isEmpty,
               s"multisample-vcfstats-$variantcaller found in summary but should not exist")
      }
    }

  @Test(dataProvider = "library-variantcallers", dependsOnGroups = Array("summary"))
  def testLibraryVariantcaller(sample: String, lib: String, variantcaller: String): Unit =
    withClue(s"Variantcaller: $variantcaller, Sample: $sample, Lib: $lib") {
      val dir =
        new File(libraryDir(sample, lib), "variantcalling" + File.separator + variantcaller)
      val file = new File(dir, s"$sample-$lib.$variantcaller.vcf.gz")
      val vcfstats = Await.result(summaryDb.getStat(runId,
                                                    "shivavariantcalling",
                                                    s"$sample-$lib-vcfstats-$variantcaller",
                                                    sample,
                                                    lib),
                                  Duration.Inf)
      if (singleSampleVariantcalling.contains(true) && variantcallers.contains(variantcaller)) {
        assert(dir.exists())
        assert(dir.isDirectory)
        assert(file.exists())
        assert(vcfstats.isDefined, s"multisample-vcfstats-$variantcaller not found in summary")
        Shiva.testSamplesVcfFile(file, List(sample))
      } else {
        assert(!dir.exists())
        assert(vcfstats.isEmpty,
               s"multisample-vcfstats-$variantcaller found in summary but should not exist")
      }
    }

  @Test(dependsOnGroups = Array("summary"))
  def testMultisampleVcfFile(): Unit = {
    val file = new File(outputDir, "variantcalling" + File.separator + "multisample.final.vcf.gz")
    testSummaryFiles(
      FileTest(shivavariantcallingGroup,
               "final",
               !multisampleVariantcalling.contains(false),
               true,
               path = file))
    if (!multisampleVariantcalling.contains(false))
      Shiva.testSamplesVcfFile(file, samples.keySet.toList)
    else assert(!file.exists())
  }

  @Test
  def testMultisampleVariantcallerInfoTag(): Unit =
    if (!multisampleVariantcalling.contains(false))
      testVariantcallerInfoTag(
        new File(outputDir, "variantcalling" + File.separator + "multisample.final.vcf.gz"))

  @Test(dataProvider = "samples", dependsOnGroups = Array("summary"))
  def testSingleSampleVcfFile(sample: String): Unit = withClue(s"Sample: $sample") {
    val file =
      new File(sampleDir(sample), "variantcalling" + File.separator + s"$sample.final.vcf.gz")
    testSummaryFiles(
      FileTest(shivavariantcallingGroup.copy(sample = sample),
               "final",
               singleSampleVariantcalling.contains(true),
               true,
               path = file))
  }

  @Test(dataProvider = "samples")
  def testSampleVariantcallerInfoTag(sample: String): Unit =
    if (singleSampleVariantcalling.contains(true))
      testVariantcallerInfoTag(
        new File(sampleDir(sample), "variantcalling" + File.separator + s"$sample.final.vcf.gz"))

  @Test(dataProvider = "libraries", dependsOnGroups = Array("summary"))
  def testLibraryVcfFile(sample: String, lib: String): Unit =
    withClue(s"Sample: $sample, Lib: $lib") {
      val file = new File(libraryDir(sample, lib),
                          "variantcalling" + File.separator + s"$sample-$lib.final.vcf.gz")
      testSummaryFiles(
        FileTest(shivavariantcallingGroup.copy(sample = sample, library = lib),
                 "final",
                 libraryVariantcalling.contains(true),
                 true,
                 path = file))
    }

  @Test(dataProvider = "libraries")
  def testLibraryVariantcallerInfoTag(sample: String, lib: String): Unit =
    if (libraryVariantcalling.contains(true))
      testVariantcallerInfoTag(
        new File(libraryDir(sample, lib),
                 "variantcalling" + File.separator + s"$sample-$lib.final.vcf.gz"))

  @Test(dataProvider = "libraries", dependsOnGroups = Array("summary"))
  def testShivaLibraryBam(sample: String, lib: String): Unit =
    withClue(s"Sample: $sample, Lib: $lib") {
      val replacejob =
        new File(libraryDir(sample, lib), s".$sample-$lib.final.bam.addorreplacereadgroups.out")
      if (replacejob.exists()) assert(!libraryBam(sample, lib).exists())
      else if (samples(sample).size == 1 && (!useIndelRealigner.getOrElse(true) && !useBaseRecalibration
                 .getOrElse(true))) {
        libraryBam(sample, lib) should exist

        val reader = SamReaderFactory.makeDefault.open(libraryBam(sample, lib))
        val header = reader.getFileHeader
        assert(!header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))
        assert(!header.getProgramRecords.exists(_.getId == "GATK PrintReads"))
        reader.close()
      } else libraryBam(sample, lib) should not be exist

    }

  @Test(dataProvider = "libraries", dependsOnGroups = Array("summary"))
  def testShivaLibraryPreprocessBam(sample: String, lib: String): Unit =
    withClue(s"Sample: $sample, Lib: $lib") {
      if (useBaseRecalibration.getOrElse(true) && usePrintReads.getOrElse(true))
        assert(!libraryPreprecoessBam(sample, lib).exists())
    }

  @Test(dataProvider = "samples", dependsOnGroups = Array("summary"))
  def testShivaSamplePreprocessBam(sample: String): Unit = withClue(s"Sample: $sample") {
    val reader = SamReaderFactory.makeDefault.open(samplePreprocessBam(sample))
    val header = reader.getFileHeader

    if (!useIndelRealigner.contains(false))
      assert(header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))
    else assert(!header.getProgramRecords.exists(_.getId == "GATK IndelRealigner"))

    if (!useBaseRecalibration.contains(false) && dbsnpVcfFile.isDefined && !usePrintReads.contains(
          false))
      assert(header.getProgramRecords.exists(_.getId == "GATK PrintReads"))
    else assert(!header.getProgramRecords.exists(_.getId == "GATK PrintReads"))

    reader.close()
  }
}
