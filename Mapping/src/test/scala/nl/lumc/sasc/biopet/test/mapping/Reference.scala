package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{ Test, BeforeClass, Factory }

/**
 * Created by pjvanthof on 06/08/15.
 */
class ReferenceSingleTemplate(aln: String, rs: String, rn: String) extends AbstractMapping {
  override def outputDir = new File(super.outputDir, s"$aln+$rs+$rn")
  override def aligner = Some(aln)
  override def referenceSpecies = Some(rs)
  override def referenceName = Some(rn)

  //TODO: add files
  override def args = super.args ++ Seq(
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath
  )
}

class ReferencePairedTemplate(aln: String, rs: String, rn: String) extends AbstractMapping {
  override def outputDir = new File(super.outputDir, s"$aln+$rs+$rn")
  override def aligner = Some(aln)
  override def referenceSpecies = Some(rs)
  override def referenceName = Some(rn)

  //TODO: add files
  override def args = super.args ++ Seq(
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath,
    "-R2", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath
  )
}

class ReferenceSingleTest extends TestNGSuite with Matchers {
  @Factory
  def createSingleClasses = {
    (for (aln <- Reference.aligners; (species, genomes) <- Reference.genomes; genome <- genomes) yield {
      new ReferenceSingleTemplate(aln, species, genome)
    }).toArray
  }
}

class ReferencePairedTest extends TestNGSuite with Matchers {
  @Factory
  def createPairedClasses = {
    (for (aln <- Reference.aligners; (species, genomes) <- Reference.genomes; genome <- genomes) yield {
      new ReferencePairedTemplate(aln, species, genome)
    }).toArray
  }
}

object Reference {
  //FIXME: not a correct list yet
  val aligners = List("bwa", "bowtie", "stampy", "tophead", "gmap")
  val genomes: Map[String, List[String]] = Map(
    "H.Sapiens" -> List("hg19"),
    "M.Musculus" -> List("mm10"),
    "R.norvegicus" -> List("rn5")
  )
}