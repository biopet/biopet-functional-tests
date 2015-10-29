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

  def outputPath: Option[String] = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") }

  @Test def testOutputFile = {
    val path = outputPath getOrElse ""
    assert(path.nonEmpty)
    val outputFile = new File(path)
    assert(outputFile.exists(), s"""Expected output file $path does not exist""")
  }

  @Test(dependsOnMethods = Array("testOutputFile"))
  def allPositionsPresent = {
    val outputReader = new VCFFileReader(new File(outputPath.get))

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

  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") }
}

trait ToucanKeepIntermediates extends ToucanSuccess {
  override def keepIntermediates = true

  def intermediates: List[File] = List(new File(this.
    inputVcf.
    map(x => x.
      getAbsolutePath.
      replaceAll(".vcf.gz", ".vep.vcf")).
    getOrElse("")))

  @Test
  def testIntermediateExistence = {
    intermediates.foreach(x => assert(x.exists(), s"""Intermediate file $x not found"""))
  }
}

trait ToucanNormalizerExplode extends ToucanSuccess {
  override def normalizerMode = "explode"
}

trait ToucanExplodeKeepIntermediates extends ToucanKeepIntermediates with ToucanNormalizerExplode

trait ToucanWithGoNL extends ToucanSuccess {
  override def goNLFile = Some(Biopet.fixtureFile("toucan" + File.separator +
    "gonl_allchroms_snpindels.sorted.chr.vcf.gz"))

  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.vcf.gz") }
}

trait ToucanWithExac extends ToucanSuccess {

  override def exacFile = Some(Biopet.fixtureFile("toucan" + File.separator + "ExAC.r0.3.sites.vep.vcf.gz"))

  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.exac.vcf.gz") }
}

trait ToucanWithGoNLAndExac extends ToucanSuccess {

  override def exacFile = Some(Biopet.fixtureFile("toucan" + File.separator + "ExAC.r0.3.sites.vep.vcf.gz"))
  override def goNLFile = Some(Biopet.fixtureFile("toucan" + File.separator +
    "gonl_allchroms_snpindels.sorted.chr.vcf.gz"))

  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.exac.vcf.gz") }
}

class ToucanGoNLIntermediateTest extends ToucanKeepIntermediates with ToucanWithGoNL
class ToucanGoNLExplodeTest extends ToucanNormalizerExplode with ToucanWithGoNL
class ToucanGoNLExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithGoNL

class ToucanExacIntermediateTest extends ToucanKeepIntermediates with ToucanWithExac
class ToucanExacExplodeTest extends ToucanNormalizerExplode with ToucanWithExac
class ToucanExacExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithExac

class ToucanGoNLExacIntermediateTest extends ToucanKeepIntermediates with ToucanWithGoNLAndExac
class ToucanGoNLExacExplodeTest extends ToucanNormalizerExplode with ToucanWithGoNLAndExac
class ToucanGoNLExacExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithGoNLAndExac

class ToucanPlainTest extends ToucanPlain
class ToucanPlainIntermediateTest extends ToucanPlain with ToucanKeepIntermediates
class ToucanPlainExplodeTest extends ToucanPlain with ToucanNormalizerExplode
