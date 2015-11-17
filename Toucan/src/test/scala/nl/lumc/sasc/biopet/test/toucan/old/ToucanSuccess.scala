package nl.lumc.sasc.biopet.test.toucan.old

import java.io.File
import java.util

import htsjdk.variant.variantcontext.VariantContext
import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.references.HsapiensHg19
import nl.lumc.sasc.biopet.test.toucan.Toucan
import nl.lumc.sasc.biopet.test.utils._
import org.testng.annotations.{ DataProvider, Test }

import scala.collection.JavaConversions._
import scala.collection.immutable.Nil

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

  @Test def testOutputFile() = {
    assert(outputPath.nonEmpty)
    val outputFile = new File(outputPath)
    assert(outputFile.exists(), s"""Expected output file $outputPath does not exist""")
  }

  @Test(dependsOnMethods = Array("testOutputFile"))
  def allPositionsPresent() = {
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
  def allPositionsIdentical() = {
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

  @DataProvider(name = "vep_field")
  def vepFields = {
    val fields = "VEP_AA_MAF" :: "VEP_AFR_MAF" :: "VEP_AMR_MAF" ::
      "VEP_ASN_MAF" :: "VEP_Allele" :: "VEP_Amino_acids" :: "VEP_BIOTYPE" ::
      "VEP_CANONICAL" :: "VEP_CCDS" :: "VEP_CDS_position" :: "VEP_CLIN_SIG" ::
      "VEP_Codons" :: "VEP_Consequence" :: "VEP_DISTANCE" :: "VEP_DOMAINS" ::
      "VEP_EA_MAF" :: "VEP_ENSP" :: "VEP_EUR_MAF" :: "VEP_EXON" :: "VEP_Existing_variation" ::
      "VEP_Feature" :: "VEP_Feature_type" :: "VEP_GMAF" :: "VEP_Gene" ::
      "VEP_HGNC_ID" :: "VEP_HGVSc" :: "VEP_HGVSp" :: "VEP_HIGH_INF_POS" ::
      "VEP_INTRON" :: "VEP_MOTIF_NAME" :: "VEP_MOTIF_POS" ::
      "VEP_MOTIF_SCORE_CHANGE" :: "VEP_PUBMED" :: "VEP_PolyPhen" ::
      "VEP_Protein_position" :: "VEP_SIFT" :: "VEP_SOMATIC" ::
      "VEP_STRAND" :: "VEP_SWISSPROT" :: "VEP_SYMBOL" ::
      "VEP_SYMBOL_SOURCE" :: "VEP_TREMBL" :: "VEP_cDNA_position" :: Nil
    fields.map(Array(_)).toArray
  }

  @Test(dataProvider = "vep_field")
  def testHasVepField(f: String) = {
    val outputReader = new VCFFileReader(new File(outputPath))
    val header = outputReader.getFileHeader
    assert(header.hasInfoLine(f), s"""Field $f does not exist in output file""")
    outputReader.close()
  }

  @Test
  def mustHaveConsequence() = {
    val outputReader = new VCFFileReader(new File(outputPath))
    outputReader foreach { x =>
      assert(x.hasAttribute("VEP_Consequence"), s"""Variant at ${x.getContig}:${x.getStart} has no VEP consequence""")
    }
    outputReader.close()
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
    v1.getContig == v2.getContig &&
      v1.getStart == v2.getStart &&
      v1.getEnd == v2.getEnd &&
      v1.getReference.equals(v2.getReference) &&
      (for ((a1, a2) <- v1.getAlternateAlleles zip v2.getAlternateAlleles)
        yield a1.equals(a2)).forall(x => x) &&
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
  def sameAmountVariants() = {
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
  def testIntermediateExistence() = {
    intermediates.foreach(x => assert(x.exists(), s"""Intermediate file $x not found"""))
  }
}

trait ToucanNormalizerExplode extends ToucanSuccess {
  override def normalizerMode = "explode"

  logMustHave(".+VepNormalizer.+explode.+".r)
  logMustNotHave(".+VepNormalizer.+standard.+".r)

  @Test
  def testAmountVariants() = {
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
  def testNormalizedAmountVariants() = {
    val outputReader = new VCFFileReader(new File(outputPath))
    val knownReader = new VCFFileReader(knownNormalized)
    val outputSize = outputReader.foldLeft(0)((ac, _) => ac + 1)
    val knownSize = knownReader.foldLeft(0)((ac, _) => ac + 1)
    assert(outputSize == knownSize)
  }
}

trait ToucanExplodeKeepIntermediates extends ToucanKeepIntermediates with ToucanNormalizerExplode

trait ToucanWithGoNL extends ToucanSuccess {

  logMustHave(".+VcfWithVcf.+AF_gonl.+".r)
  logMustNotHave(".+VcfWithVcf.+AF_exac.+".r)

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
  def testAFField() = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_gonl"))
    // not all records will have an annotation
    //reader.foreach(x => assert(x.hasAttribute("AF_gonl")))
    reader.close()
  }

  @Test
  def testAFFieldType() = {
    val reader = new VCFFileReader(new File(outputPath))
    val allDoubles = reader filter { r => r.hasAttribute("AF_gonl") } map { x => x.getAttribute("AF_gonl"): Any } map {
      case l if l.isInstanceOf[util.ArrayList[_]] => l.asInstanceOf[util.ArrayList[_]].toList map {
        case elD: Double => true
        case elS: String => toDouble(elS).nonEmpty
        case _           => false
      } forall { y => y }
      case d: Double => true
      case s: String => toDouble(s).nonEmpty
      case _         => false
    } forall { z => z }
    assert(allDoubles)
    reader.close()
  }
}

trait ToucanWithExac extends ToucanSuccess {

  logMustNotHave(".+VcfWithVcf.+AF_gonl.+".r)
  logMustHave(".+VcfWithVcf.+AF_exac.+".r)

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
  def testAFField() = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_exac"))
    // not all records will have an annotation
    //reader.foreach(x => assert(x.hasAttribute("AF_exac")))
    reader.close()
  }

  @Test
  def testAFFieldType() = {
    val reader = new VCFFileReader(new File(outputPath))
    val allDoubles = reader filter { r => r.hasAttribute("AF_exac") } map { x => x.getAttribute("AF_exac"): Any } map {
      case l: util.ArrayList[_] => l forall {
        case elD: Double => true
        case elS: String => toDouble(elS).nonEmpty
        case _           => false
      }
      case d: Double => true
      case s: String => toDouble(s).nonEmpty
      case _         => false
    } forall { z => z }
    assert(allDoubles)
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
  def testGoNLAFField() = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_gonl"))
    //reader.foreach(x => assert(x.hasAttribute("AF_gonl")))
    reader.close()
  }

  @Test
  def testExacAFField() = {
    val reader = new VCFFileReader(new File(outputPath))
    assert(reader.getFileHeader.hasInfoLine("AF_exac"))
    //reader.foreach(x => assert(x.hasAttribute("AF_exac")))
    reader.close()
  }
  @Test
  def testExacAFFieldType() = {
    val reader = new VCFFileReader(new File(outputPath))
    val allDoubles = reader filter { r => r.hasAttribute("AF_exac") } map { x => x.getAttribute("AF_exac"): Any } map {
      case l: util.ArrayList[_] => l forall {
        case elD: Double => true
        case elS: String => toDouble(elS).nonEmpty
        case _           => false
      }
      case d: Double => true
      case s: String => toDouble(s).nonEmpty
      case _         => false
    } forall { z => z }
    assert(allDoubles)
    reader.close()
  }

  @Test
  def testGoNLAFFieldType() = {
    val reader = new VCFFileReader(new File(outputPath))
    val allDoubles = reader filter { r => r.hasAttribute("AF_gonl") } map { x => x.getAttribute("AF_gonl"): Any } map {
      case l: util.ArrayList[_] => l forall {
        case elD: Double => true
        case elS: String => toDouble(elS).nonEmpty
        case _           => false
      }
      case d: Double => true
      case s: String => toDouble(s).nonEmpty
      case _         => false
    } forall { z => z }
    assert(allDoubles)
    reader.close()
  }
}

class ToucanGoNLPlainTest extends ToucanPlain with ToucanWithGoNL with HsapiensHg19
class ToucanGoNLIntermediateTest extends ToucanKeepIntermediates with ToucanWithGoNL with HsapiensHg19
class ToucanGoNLExplodeTest extends ToucanNormalizerExplode with ToucanWithGoNL with HsapiensHg19
class ToucanGoNLExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithGoNL with HsapiensHg19

class ToucanExacPlainTest extends ToucanPlain with ToucanWithExac with HsapiensHg19
class ToucanExacIntermediateTest extends ToucanKeepIntermediates with ToucanWithExac with HsapiensHg19
class ToucanExacExplodeTest extends ToucanNormalizerExplode with ToucanWithExac with HsapiensHg19
class ToucanExacExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithExac with HsapiensHg19

class ToucanGoNLExacPlainTest extends ToucanPlain with ToucanWithGoNLAndExac with HsapiensHg19
class ToucanGoNLExacIntermediateTest extends ToucanKeepIntermediates with ToucanWithGoNLAndExac with HsapiensHg19
class ToucanGoNLExacExplodeTest extends ToucanNormalizerExplode with ToucanWithGoNLAndExac with HsapiensHg19
class ToucanGoNLExacExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with ToucanWithGoNLAndExac with HsapiensHg19

class ToucanPlainTest extends ToucanPlain with HsapiensHg19
class ToucanPlainIntermediateTest extends ToucanPlain with ToucanKeepIntermediates with HsapiensHg19
class ToucanPlainExplodeTest extends ToucanNormalizerExplode with HsapiensHg19
class ToucanPlainExplodeIntermediateTest extends ToucanExplodeKeepIntermediates with HsapiensHg19
