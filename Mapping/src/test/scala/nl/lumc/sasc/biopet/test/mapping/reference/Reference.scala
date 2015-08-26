package nl.lumc.sasc.biopet.test.mapping.reference

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.mapping.AbstractMapping
import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{DataProvider, Factory}

/**
 * Created by pjvanthof on 06/08/15.
 */
class ReferenceSingleTemplate(aln: String, rs: String, rn: String) extends AbstractMapping {
  override def aligner = Some(aln)
  override def referenceSpecies = Some(rs)
  override def referenceName = Some(rn)

  //TODO: add files
  override def args = super.args ++ Seq("-run", "-cv", "skip_flexiprep=true", "-cv", "skip_metrics=true",
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath
  )
}

class ReferencePairedTemplate(aln: String, rs: String, rn: String) extends AbstractMapping {
  override def aligner = Some(aln)
  override def referenceSpecies = Some(rs)
  override def referenceName = Some(rn)

  //TODO: add files
  override def args = super.args ++ Seq("-run", "-cv", "skip_flexiprep=true", "-cv", "skip_metrics=true",
    "-R1", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz").getAbsolutePath,
    "-R2", Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz").getAbsolutePath
  )
}

class ReferenceSingleBwamemHsapiensGRCh38Test extends ReferenceSingleTemplate("bwa-mem", "H.sapiens", "GRCh38")
class ReferenceSingleBwamemHsapiensGRCh37Test extends ReferenceSingleTemplate("bwa-mem", "H.sapiens", "GRCh37")
class ReferenceSingleBwamemMmusculusGRCm38Test extends ReferenceSingleTemplate("bwa-mem", "M.musculus", "GRCm38")
class ReferenceSingleBwamemRnorvegicusRnor_6Test extends ReferenceSingleTemplate("bwa-mem", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleBwamemCelegansWBcel235Test extends ReferenceSingleTemplate("bwa-mem", "C.elegans", "WBcel235")

class ReferenceSingleBowtieHsapiensGRCh38Test extends ReferenceSingleTemplate("bowtie", "H.sapiens", "GRCh38")
class ReferenceSingleBowtieHsapiensGRCh37Test extends ReferenceSingleTemplate("bowtie", "H.sapiens", "GRCh37")
class ReferenceSingleBowtieMmusculusGRCm38Test extends ReferenceSingleTemplate("bowtie", "M.musculus", "GRCm38")
class ReferenceSingleBowtieRnorvegicusRnor_6Test extends ReferenceSingleTemplate("bowtie", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleBowtieCelegansWBcel235Test extends ReferenceSingleTemplate("bowtie", "C.elegans", "WBcel235")

class ReferenceSingleTophatHsapiensGRCh38Test extends ReferenceSingleTemplate("tophat", "H.sapiens", "GRCh38")
class ReferenceSingleTophatHsapiensGRCh37Test extends ReferenceSingleTemplate("tophat", "H.sapiens", "GRCh37")
class ReferenceSingleTophatMmusculusGRCm38Test extends ReferenceSingleTemplate("tophat", "M.musculus", "GRCm38")
class ReferenceSingleTophatRnorvegicusRnor_6Test extends ReferenceSingleTemplate("tophat", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleTophatCelegansWBcel235Test extends ReferenceSingleTemplate("tophat", "C.elegans", "WBcel235")

class ReferenceSingleGsnapHsapiensGRCh38Test extends ReferenceSingleTemplate("gsnap", "H.sapiens", "GRCh38")
class ReferenceSingleGsnapHsapiensGRCh37Test extends ReferenceSingleTemplate("gsnap", "H.sapiens", "GRCh37")
class ReferenceSingleGsnapMmusculusGRCm38Test extends ReferenceSingleTemplate("gsnap", "M.musculus", "GRCm38")
class ReferenceSingleGsnapRnorvegicusRnor_6Test extends ReferenceSingleTemplate("gsnap", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleGsnapCelegansWBcel235Test extends ReferenceSingleTemplate("gsnap", "C.elegans", "WBcel235")

class ReferenceSingleStarHsapiensGRCh38Test extends ReferenceSingleTemplate("star", "H.sapiens", "GRCh38")
class ReferenceSingleStarHsapiensGRCh37Test extends ReferenceSingleTemplate("star", "H.sapiens", "GRCh37")
class ReferenceSingleStarMmusculusGRCm38Test extends ReferenceSingleTemplate("star", "M.musculus", "GRCm38")
class ReferenceSingleStarRnorvegicusRnor_6Test extends ReferenceSingleTemplate("star", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleStarCelegansWBcel235Test extends ReferenceSingleTemplate("star", "C.elegans", "WBcel235")

class ReferencePairedBwamemHsapiensGRCh38Test extends ReferencePairedTemplate("bwa-mem", "H.sapiens", "GRCh38")
class ReferencePairedBwamemHsapiensGRCh37Test extends ReferencePairedTemplate("bwa-mem", "H.sapiens", "GRCh37")
class ReferencePairedBwamemMmusculusGRCm38Test extends ReferencePairedTemplate("bwa-mem", "M.musculus", "GRCm38")
class ReferencePairedBwamemRnorvegicusRnor_6Test extends ReferencePairedTemplate("bwa-mem", "R.norvegicus", "Rnor_6.0")
class ReferencePairedBwamemCelegansWBcel235Test extends ReferencePairedTemplate("bwa-mem", "C.elegans", "WBcel235")

class ReferencePairedBowtieHsapiensGRCh38Test extends ReferencePairedTemplate("bowtie", "H.sapiens", "GRCh38")
class ReferencePairedBowtieHsapiensGRCh37Test extends ReferencePairedTemplate("bowtie", "H.sapiens", "GRCh37")
class ReferencePairedBowtieMmusculusGRCm38Test extends ReferencePairedTemplate("bowtie", "M.musculus", "GRCm38")
class ReferencePairedBowtieRnorvegicusRnor_6Test extends ReferencePairedTemplate("bowtie", "R.norvegicus", "Rnor_6.0")
class ReferencePairedBowtieCelegansWBcel235Test extends ReferencePairedTemplate("bowtie", "C.elegans", "WBcel235")

class ReferencePairedTophatHsapiensGRCh38Test extends ReferencePairedTemplate("tophat", "H.sapiens", "GRCh38")
class ReferencePairedTophatHsapiensGRCh37Test extends ReferencePairedTemplate("tophat", "H.sapiens", "GRCh37")
class ReferencePairedTophatMmusculusGRCm38Test extends ReferencePairedTemplate("tophat", "M.musculus", "GRCm38")
class ReferencePairedTophatRnorvegicusRnor_6Test extends ReferencePairedTemplate("tophat", "R.norvegicus", "Rnor_6.0")
class ReferencePairedTophatCelegansWBcel235Test extends ReferencePairedTemplate("tophat", "C.elegans", "WBcel235")

class ReferencePairedGsnapHsapiensGRCh38Test extends ReferencePairedTemplate("gsnap", "H.sapiens", "GRCh38")
class ReferencePairedGsnapHsapiensGRCh37Test extends ReferencePairedTemplate("gsnap", "H.sapiens", "GRCh37")
class ReferencePairedGsnapMmusculusGRCm38Test extends ReferencePairedTemplate("gsnap", "M.musculus", "GRCm38")
class ReferencePairedGsnapRnorvegicusRnor_6Test extends ReferencePairedTemplate("gsnap", "R.norvegicus", "Rnor_6.0")
class ReferencePairedGsnapCelegansWBcel235Test extends ReferencePairedTemplate("gsnap", "C.elegans", "WBcel235")

class ReferencePairedStarHsapiensGRCh38Test extends ReferencePairedTemplate("star", "H.sapiens", "GRCh38")
class ReferencePairedStarHsapiensGRCh37Test extends ReferencePairedTemplate("star", "H.sapiens", "GRCh37")
class ReferencePairedStarMmusculusGRCm38Test extends ReferencePairedTemplate("star", "M.musculus", "GRCm38")
class ReferencePairedStarRnorvegicusRnor_6Test extends ReferencePairedTemplate("star", "R.norvegicus", "Rnor_6.0")
class ReferencePairedStarCelegansWBcel235Test extends ReferencePairedTemplate("star", "C.elegans", "WBcel235")

/*
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
*/

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