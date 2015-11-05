package nl.lumc.sasc.biopet.test.toucan

import java.io.File

import htsjdk.variant.variantcontext.VariantContext
import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Biopet
import org.testng.annotations.Test
import scala.collection.JavaConversions._

/**
 * Created by ahbbollen on 22-10-15.
 */
trait ToucanSuccess extends Toucan {

  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))

  def knownNormalized: File = Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vep.standard.vcf.gz")

  logMustNotHave("""Script failed with \d+ total jobs""".r)
  logMustHave("""Script completed successfully with \d+ total jobs""".r)

  def outputPath: String = outputDir.getAbsolutePath +
    File.separator +
    (this.inputVcf map
      { x => x.getName } map
      { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") } getOrElse "")
  def intermediates: List[File] = List(new File(this.
    outputDir.getAbsolutePath + File.separator + inputVcf.
    map(x => x.
      getName.
      replaceAll(".vcf.gz", ".vep.vcf")).
    getOrElse("")))

  @Test def testOutputFile = {
    assert(outputPath.nonEmpty)
    val outputFile = new File(outputPath)
    assert(outputFile.exists(), s"""Expected output file $outputPath does not exist""")
  }

  @Test(dependsOnMethods = Array("testOutputFile"))
  def allPositionsPresent = {
    val outputReader = new VCFFileReader(new File(outputPath))

    inputVcf map
      { x => new VCFFileReader(x) } map
      { x => x.iterator() } foreach
      { x =>
        x foreach
          { y =>
            val query = outputReader.query(y.getContig, y.getStart, y.getEnd)
            assert(query.nonEmpty, s"""Position ${y.getStart} not found on contig ${y.getContig} in output file""")
          }
      }
  }

  @Test
  def allPositionsIdentical = {
    val outputReader = new VCFFileReader(new File(outputPath))

    val inputReader = new VCFFileReader(inputVcf getOrElse new File(""))

    inputReader foreach { record =>
      val q1 = inputReader.query(record.getContig, record.getStart, record.getEnd)
      val q2 = outputReader.query(record.getContig, record.getStart, record.getEnd)

      val compare = q1 map { x =>
        q2 exists { y =>
          sameVariant(x, y)
        }
      } forall (z => z)

      assert(compare, s"""Variant at ${record.getContig}:${record.getStart} has no identical variant in output file""")
    }
  }

  /**
   * Tests whether general information of variant is the same
   * That is:
   * Position, REF, ALT and Genotypes (GT, GQ, DP and PL)
   * @param v1 variant 1
   * @param v2 variant 2
   * @return true if identical, false if not
   */
  def sameVariant(v1: VariantContext, v2: VariantContext): Boolean = {
    val sameAltAllele = (for ((a1, a2) <- v1.getAlternateAlleles zip v2.getAlternateAlleles)
      yield a1.equals(a2)).forall(x => x)
    v1.getContig == v2.getContig &&
      v1.getStart == v2.getStart &&
      v1.getEnd == v2.getEnd &&
      v1.getReference.equals(v2.getReference) &&
      sameAltAllele &&
      v1.getSampleNamesOrderedByName == v2.getSampleNamesOrderedByName &&
      (for ((n1, n2) <- v1.getSampleNamesOrderedByName zip v2.getSampleNamesOrderedByName)
        yield v1.getGenotype(n1).getDP == v2.getGenotype(n2).getDP &&
        v1.getGenotype(n1).getAD.toList == v2.getGenotype(n2).getAD.toList &&
        v1.getGenotype(n1).getType.toString == v2.getGenotype(n2).getType.toString &&
        v1.getGenotype(n1).getLikelihoodsString == v2.getGenotype(n2).getLikelihoodsString &&
        v1.getGenotype(n1).getGQ == v2.getGenotype(n2).getGQ &&
        v1.getGenotype(n1).getGenotypeString == v2.getGenotype(n2).getGenotypeString).forall(x => x)
  }
}

trait ToucanPlain extends ToucanSuccess {

  logMustNotHave(".+VepNormalizer.+explode.+".r)
  logMustHave(".+VepNormalizer.+standard.+".r)

  override def outputPath = outputDir.getAbsolutePath +
    File.separator +
    (this.inputVcf map
      { x => x.getName } map
      { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") } getOrElse "")

  @Test
  def sameAmountVariants = {
    val inputReader = new VCFFileReader(inputVcf getOrElse new File(""))
    val inputSize = inputReader.foldLeft(0)((ac, _) => ac + 1)
    val outputReader = new VCFFileReader(new File(outputPath))
    val outputSize = outputReader.foldLeft(0)((ac, _) => ac + 1)
    assert(inputSize == outputSize)
    inputReader.close()
    outputReader.close()
  }
}

trait ToucanKeepIntermediates extends ToucanSuccess {
  override def keepIntermediates = true

  @Test
  def testIntermediateExistence = {
    intermediates.foreach(x => assert(x.exists(), s"""Intermediate file $x not found"""))
  }
}

trait ToucanNormalizerExplode extends ToucanSuccess {
  override def normalizerMode = "explode"

  logMustHave(".+VepNormalizer.+explode.+".r)
  logMustNotHave(".+VepNormalizer.+standard.+".r)

  @Test
  def testAmountVariants = {
    val inputReader = new VCFFileReader(inputVcf getOrElse new File(""))
    val outputReader = new VCFFileReader(new File(outputPath))
    val inputSize = inputReader.foldLeft(0)((ac, _) => ac + 1)
    val outputSize = outputReader.foldLeft(0)((ac, _) => ac + 1)
    assert(outputSize >= inputSize)
    inputReader.close()
    outputReader.close()
  }

  override def knownNormalized = {
    Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vep.exploded.vcf.gz")
  }

  @Test
  def testNormalizedAmountVariants = {
    val outputReader = new VCFFileReader(new File(outputPath))
    val knownReader = new VCFFileReader(knownNormalized)
    val outputSize = outputReader.foldLeft(0)((ac, _) => ac + 1)
    val knownSize = knownReader.foldLeft(0)((ac, _) => ac + 1)
    assert(outputSize == knownSize)
  }
}

trait ToucanExplodeKeepIntermediates extends ToucanKeepIntermediates with ToucanNormalizerExplode

trait ToucanWithGoNL extends ToucanSuccess {
  override def goNLFile = Some(Biopet.fixtureFile("toucan" + File.separator +
    "gonl_allchroms_snpindels.sorted.chr.vcf.gz"))

  override def outputPath = outputDir.getAbsolutePath +
    File.separator +
    (this.inputVcf map { x => x.getName } map { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.vcf.gz") } getOrElse "")

  override def intermediates = {
    val norm = outputDir.getAbsolutePath +
      File.separator +
      (this.inputVcf map { x => x.getName } map { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") } getOrElse "")
    super.intermediates :+ new File(norm)
  }

  @Test
  def testAFField = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_gonl"))
    // not all records will have an annotation
    //reader.foreach(x => assert(x.hasAttribute("AF_gonl")))
    reader.close()
  }
}

trait ToucanWithExac extends ToucanSuccess {

  override def exacFile = Some(Biopet.fixtureFile("toucan" + File.separator + "ExAC.r0.3.sites.vep.vcf.gz"))

  override def outputPath = outputDir.getAbsolutePath +
    File.separator +
    (inputVcf map { x => x.getName } map { x => x.replaceAll(".vcf.gz$", ".vep.normalized.exac.vcf.gz") } getOrElse "")

  override def intermediates = {
    val norm = outputDir.getAbsolutePath +
      File.separator +
      (this.inputVcf map { x => x.getName } map { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") } getOrElse "")
    super.intermediates :+ new File(norm)
  }

  @Test
  def testAFField = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_exac"))
    // not all records will have an annotation
    //reader.foreach(x => assert(x.hasAttribute("AF_exac")))
    reader.close()
  }
}

trait ToucanWithGoNLAndExac extends ToucanSuccess {

  override def exacFile = Some(Biopet.fixtureFile("toucan" + File.separator + "ExAC.r0.3.sites.vep.vcf.gz"))

  override def goNLFile = Some(Biopet.fixtureFile("toucan" + File.separator +
    "gonl_allchroms_snpindels.sorted.chr.vcf.gz"))

  override def outputPath = outputDir.getAbsolutePath +
    File.separator +
    (this.inputVcf map { x => x.getName } map { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.exac.vcf.gz") } getOrElse "")

  override def intermediates = {
    val norm = outputDir.getAbsolutePath +
      File.separator +
      (this.inputVcf map { x => x.getName } map { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") } getOrElse "")
    val gonl = outputDir.getAbsolutePath +
      File.separator +
      (this.inputVcf map { x => x.getName } map { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.vcf.gz") } getOrElse "")
    super.intermediates :+ new File(norm) :+ new File(gonl)
  }

  @Test
  def testGoNLAFField = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_gonl"))
    //reader.foreach(x => assert(x.hasAttribute("AF_gonl")))
    reader.close()
  }

  @Test
  def testExacAFField = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_exac"))
    //reader.foreach(x => assert(x.hasAttribute("AF_exac")))
    reader.close()
  }
}

class ToucanGoNLPlainTest extends ToucanPlain with ToucanWithGoNL
class ToucanGoNLIntermediateTest extends ToucanKeepIntermediates with ToucanWithGoNL
class ToucanGoNLExplodeTest extends ToucanNormalizerExplode with ToucanWithGoNL
class ToucanGoNLExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithGoNL

class ToucanExacPlainTest extends ToucanPlain with ToucanWithExac
class ToucanExacIntermediateTest extends ToucanKeepIntermediates with ToucanWithExac
class ToucanExacExplodeTest extends ToucanNormalizerExplode with ToucanWithExac
class ToucanExacExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithExac

class ToucanGoNLExacPlainTest extends ToucanPlain with ToucanWithGoNLAndExac
class ToucanGoNLExacIntermediateTest extends ToucanKeepIntermediates with ToucanWithGoNLAndExac
class ToucanGoNLExacExplodeTest extends ToucanNormalizerExplode with ToucanWithGoNLAndExac
class ToucanGoNLExacExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithGoNLAndExac

class ToucanPlainTest extends ToucanPlain
class ToucanPlainIntermediateTest extends ToucanPlain with ToucanKeepIntermediates
class ToucanPlainExplodeTest extends ToucanNormalizerExplode
class ToucanPlainExplodeIntermediateTest extends ToucanExplodeKeepIntermediates
