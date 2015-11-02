package nl.lumc.sasc.biopet.test.toucan

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Biopet
import org.testng.annotations.Test
import scala.collection.JavaConversions._

/**
 * Created by ahbbollen on 22-10-15.
 */
trait ToucanSuccess extends Toucan {

  override def inputVcf = Some(Biopet.fixtureFile("toucan" + File.separator + "two_vars_each_chrom_human.vcf.gz"))

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

}

trait ToucanPlain extends ToucanSuccess {

  override def outputPath = outputDir.getAbsolutePath +
    File.separator +
    (this.inputVcf map
      { x => x.getName } map
      { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") } getOrElse "")

  @Test
  def sameAmountVariants = {
    val inputReader = new VCFFileReader(inputVcf getOrElse new File(""))
    val outputReader = new VCFFileReader(new File(outputPath))
    assert(inputReader.size == outputReader.size)
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

  @Test
  def testEqualOrGreaterAmountVariants = {
    val inputReader = new VCFFileReader(inputVcf getOrElse new File(""))
    val outputReader = new VCFFileReader(new File(outputPath))
    assert(outputReader.size >= inputReader.size)
    inputReader.close()
    outputReader.close()
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
    reader.foreach(x => assert(x.hasAttribute("AF_gonl")))
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
    reader.foreach(x => assert(x.hasAttribute("AF_exac")))
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
    reader.foreach(x => assert(x.hasAttribute("AF_gonl")))
    reader.close()
  }

  @Test
  def testExacAFField = {
    val reader = new VCFFileReader(new File(outputPath))
    reader.foreach(x => assert(x.hasAttribute("AF_exac")))
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
class ToucanPlainExplodeTest extends ToucanPlain with ToucanNormalizerExplode
class ToucanPlainExplodeIntermediateTest extends ToucanPlain with ToucanExplodeKeepIntermediates
