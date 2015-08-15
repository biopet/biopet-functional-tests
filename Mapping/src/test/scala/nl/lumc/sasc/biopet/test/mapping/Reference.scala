package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{ DataProvider, Test, BeforeClass, Factory }

/**
 * Created by pjvanthof on 06/08/15.
 */
class ReferenceSingleTemplate(aln: String, rs: String, rn: String) extends AbstractMapping {
  override def outputDir = new File(super.outputDir, s"$aln-$rs-$rn")
  override def aligner = Some(aln)
  override def referenceSpecies = Some(rs)
  override def referenceName = Some(rn)

  //TODO: add files
  override def args = super.args ++ Seq("-run", "-cv", "skip_flexiprep=true", "-cv", "skip_metrics=true",
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath
  )
}

class ReferencePairedTemplate(aln: String, rs: String, rn: String) extends AbstractMapping {
  override def outputDir = new File(super.outputDir, s"$aln-$rs-$rn")
  override def aligner = Some(aln)
  override def referenceSpecies = Some(rs)
  override def referenceName = Some(rn)

  //TODO: add files
  override def args = super.args ++ Seq("-run", "-cv", "skip_flexiprep=true", "-cv", "skip_metrics=true",
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath,
    "-R2", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath
  )
}

class ReferenceSingleTest extends TestNGSuite with Matchers {
  @DataProvider(name = "alningers-geomes", parallel = true)
  def provider = {
    (for (aln <- Reference.aligners; (species, genomes) <- Reference.genomes; genome <- genomes) yield Array(aln, species, genome)).toArray
  }

  @Factory(dataProvider = "alningers-geomes")
  def createPairedClasses(aln: String, species: String, genome: String) =
    Array(new ReferenceSingleTemplate(aln, species, genome))
}

class ReferencePairedTest extends TestNGSuite with Matchers {
  @DataProvider(name = "alningers-geomes", parallel = true)
  def provider = {
    (for (aln <- Reference.aligners; (species, genomes) <- Reference.genomes; genome <- genomes) yield Array(aln, species, genome)).toArray
  }

  @Factory(dataProvider = "alningers-geomes")
  def createPairedClasses(aln: String, species: String, genome: String) =
    Array(new ReferencePairedTemplate(aln, species, genome))
}

object Reference {
  //FIXME: not a correct list yet
  val aligners = List("bwa-mem", "bowtie", "tophat", "gsnap", "star")
  val genomes: Map[String, List[String]] = Map(
    "H.sapiens" -> List("GRCh38", "GRCh37"),
    "M.musculus" -> List("GRCm38"),
    "R.norvegicus" -> List("Rnor_6.0"),
    "C.elegans" -> List("WBcel235")
  )
}