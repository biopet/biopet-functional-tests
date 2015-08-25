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

class ReferenceSingleBwamemHsapiensGRCh38 extends ReferenceSingleTemplate("bwa-mem", "H.sapiens", "GRCh38")
class ReferenceSingleBwamemHsapiensGRCh37 extends ReferenceSingleTemplate("bwa-mem", "H.sapiens", "GRCh37")
class ReferenceSingleBwamemMmusculusGRCm38 extends ReferenceSingleTemplate("bwa-mem", "M.musculus", "GRCm38")
class ReferenceSingleBwamemRnorvegicusRnor_6 extends ReferenceSingleTemplate("bwa-mem", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleBwamemCelegansWBcel235 extends ReferenceSingleTemplate("bwa-mem", "C.elegans", "WBcel235")

class ReferenceSingleBowtieHsapiensGRCh38 extends ReferenceSingleTemplate("bowtie", "H.sapiens", "GRCh38")
class ReferenceSingleBowtieHsapiensGRCh37 extends ReferenceSingleTemplate("bowtie", "H.sapiens", "GRCh37")
class ReferenceSingleBowtieMmusculusGRCm38 extends ReferenceSingleTemplate("bowtie", "M.musculus", "GRCm38")
class ReferenceSingleBowtieRnorvegicusRnor_6 extends ReferenceSingleTemplate("bowtie", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleBowtieCelegansWBcel235 extends ReferenceSingleTemplate("bowtie", "C.elegans", "WBcel235")

class ReferenceSingleTophatHsapiensGRCh38 extends ReferenceSingleTemplate("tophat", "H.sapiens", "GRCh38")
class ReferenceSingleTophatHsapiensGRCh37 extends ReferenceSingleTemplate("tophat", "H.sapiens", "GRCh37")
class ReferenceSingleTophatMmusculusGRCm38 extends ReferenceSingleTemplate("tophat", "M.musculus", "GRCm38")
class ReferenceSingleTophatRnorvegicusRnor_6 extends ReferenceSingleTemplate("tophat", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleTophatCelegansWBcel235 extends ReferenceSingleTemplate("tophat", "C.elegans", "WBcel235")

class ReferenceSingleGsnapHsapiensGRCh38 extends ReferenceSingleTemplate("gsnap", "H.sapiens", "GRCh38")
class ReferenceSingleGsnapHsapiensGRCh37 extends ReferenceSingleTemplate("gsnap", "H.sapiens", "GRCh37")
class ReferenceSingleGsnapMmusculusGRCm38 extends ReferenceSingleTemplate("gsnap", "M.musculus", "GRCm38")
class ReferenceSingleGsnapRnorvegicusRnor_6 extends ReferenceSingleTemplate("gsnap", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleGsnapCelegansWBcel235 extends ReferenceSingleTemplate("gsnap", "C.elegans", "WBcel235")

class ReferenceSingleStarHsapiensGRCh38 extends ReferenceSingleTemplate("star", "H.sapiens", "GRCh38")
class ReferenceSingleStarHsapiensGRCh37 extends ReferenceSingleTemplate("star", "H.sapiens", "GRCh37")
class ReferenceSingleStarMmusculusGRCm38 extends ReferenceSingleTemplate("star", "M.musculus", "GRCm38")
class ReferenceSingleStarRnorvegicusRnor_6 extends ReferenceSingleTemplate("star", "R.norvegicus", "Rnor_6.0")
class ReferenceSingleStarCelegansWBcel235 extends ReferenceSingleTemplate("star", "C.elegans", "WBcel235")

class ReferencePairedBwamemHsapiensGRCh38 extends ReferencePairedTemplate("bwa-mem", "H.sapiens", "GRCh38")
class ReferencePairedBwamemHsapiensGRCh37 extends ReferencePairedTemplate("bwa-mem", "H.sapiens", "GRCh37")
class ReferencePairedBwamemMmusculusGRCm38 extends ReferencePairedTemplate("bwa-mem", "M.musculus", "GRCm38")
class ReferencePairedBwamemRnorvegicusRnor_6 extends ReferencePairedTemplate("bwa-mem", "R.norvegicus", "Rnor_6.0")
class ReferencePairedBwamemCelegansWBcel235 extends ReferencePairedTemplate("bwa-mem", "C.elegans", "WBcel235")

class ReferencePairedBowtieHsapiensGRCh38 extends ReferencePairedTemplate("bowtie", "H.sapiens", "GRCh38")
class ReferencePairedBowtieHsapiensGRCh37 extends ReferencePairedTemplate("bowtie", "H.sapiens", "GRCh37")
class ReferencePairedBowtieMmusculusGRCm38 extends ReferencePairedTemplate("bowtie", "M.musculus", "GRCm38")
class ReferencePairedBowtieRnorvegicusRnor_6 extends ReferencePairedTemplate("bowtie", "R.norvegicus", "Rnor_6.0")
class ReferencePairedBowtieCelegansWBcel235 extends ReferencePairedTemplate("bowtie", "C.elegans", "WBcel235")

class ReferencePairedTophatHsapiensGRCh38 extends ReferencePairedTemplate("tophat", "H.sapiens", "GRCh38")
class ReferencePairedTophatHsapiensGRCh37 extends ReferencePairedTemplate("tophat", "H.sapiens", "GRCh37")
class ReferencePairedTophatMmusculusGRCm38 extends ReferencePairedTemplate("tophat", "M.musculus", "GRCm38")
class ReferencePairedTophatRnorvegicusRnor_6 extends ReferencePairedTemplate("tophat", "R.norvegicus", "Rnor_6.0")
class ReferencePairedTophatCelegansWBcel235 extends ReferencePairedTemplate("tophat", "C.elegans", "WBcel235")

class ReferencePairedGsnapHsapiensGRCh38 extends ReferencePairedTemplate("gsnap", "H.sapiens", "GRCh38")
class ReferencePairedGsnapHsapiensGRCh37 extends ReferencePairedTemplate("gsnap", "H.sapiens", "GRCh37")
class ReferencePairedGsnapMmusculusGRCm38 extends ReferencePairedTemplate("gsnap", "M.musculus", "GRCm38")
class ReferencePairedGsnapRnorvegicusRnor_6 extends ReferencePairedTemplate("gsnap", "R.norvegicus", "Rnor_6.0")
class ReferencePairedGsnapCelegansWBcel235 extends ReferencePairedTemplate("gsnap", "C.elegans", "WBcel235")

class ReferencePairedStarHsapiensGRCh38 extends ReferencePairedTemplate("star", "H.sapiens", "GRCh38")
class ReferencePairedStarHsapiensGRCh37 extends ReferencePairedTemplate("star", "H.sapiens", "GRCh37")
class ReferencePairedStarMmusculusGRCm38 extends ReferencePairedTemplate("star", "M.musculus", "GRCm38")
class ReferencePairedStarRnorvegicusRnor_6 extends ReferencePairedTemplate("star", "R.norvegicus", "Rnor_6.0")
class ReferencePairedStarCelegansWBcel235 extends ReferencePairedTemplate("star", "C.elegans", "WBcel235")

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