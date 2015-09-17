package nl.lumc.sasc.biopet.test.mapping.reference

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.mapping.AbstractMapping
import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{ DataProvider, Factory }

/**
 * Created by pjvanthof on 06/08/15.
 */
class ReferenceSingleTemplate(aln: String) extends AbstractMapping {
  override def aligner = Some(aln)
  override def functionalTest = true
  override def args = super.args ++ Seq("-run", "-cv", "skip_flexiprep=true", "-cv", "skip_metrics=true"
  )
}

class ReferencePairedTemplate(aln: String) extends AbstractMapping {
  override def aligner = Some(aln)
  override def functionalTest = true
  override def args = super.args ++ Seq("-run", "-cv", "skip_flexiprep=true", "-cv", "skip_metrics=true")
}

trait HsapiensGRCh38 extends AbstractMapping {
  override def referenceSpecies = Some("H.sapiens")
  override def referenceName = Some("GRCh38")
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait HsapiensGRCh37 extends AbstractMapping {
  override def referenceSpecies = Some("H.sapiens")
  override def referenceName = Some("GRCh37")
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait MmusculusGRCm38 extends AbstractMapping {
  override def referenceSpecies = Some("M.musculus")
  override def referenceName = Some("GRCm38")

  //TODO: M.musculus specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait RnorvegicusRnor_6 extends AbstractMapping {
  override def referenceSpecies = Some("R.norvegicus")
  override def referenceName = Some("Rnor_6.0")

  //TODO: R.norvegicus specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait CelegansWBcel235 extends AbstractMapping {
  override def referenceSpecies = Some("C.elegans")
  override def referenceName = Some("WBcel235")

  //TODO: C.elegans specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

class SingleBwamemHsapiensGRCh38Test extends ReferenceSingleTemplate("bwa-mem") with HsapiensGRCh38
class SingleBwamemHsapiensGRCh37Test extends ReferenceSingleTemplate("bwa-mem") with HsapiensGRCh37
class SingleBwamemMmusculusGRCm38Test extends ReferenceSingleTemplate("bwa-mem") with MmusculusGRCm38
class SingleBwamemRnorvegicusRnor_6Test extends ReferenceSingleTemplate("bwa-mem") with RnorvegicusRnor_6
class SingleBwamemCelegansWBcel235Test extends ReferenceSingleTemplate("bwa-mem") with CelegansWBcel235

class SingleBowtieHsapiensGRCh38Test extends ReferenceSingleTemplate("bowtie") with HsapiensGRCh38
class SingleBowtieHsapiensGRCh37Test extends ReferenceSingleTemplate("bowtie") with HsapiensGRCh37
class SingleBowtieMmusculusGRCm38Test extends ReferenceSingleTemplate("bowtie") with MmusculusGRCm38
class SingleBowtieRnorvegicusRnor_6Test extends ReferenceSingleTemplate("bowtie") with RnorvegicusRnor_6
class SingleBowtieCelegansWBcel235Test extends ReferenceSingleTemplate("bowtie") with CelegansWBcel235

class SingleTophatHsapiensGRCh38Test extends ReferenceSingleTemplate("tophat") with HsapiensGRCh38
class SingleTophatHsapiensGRCh37Test extends ReferenceSingleTemplate("tophat") with HsapiensGRCh37
//class SingleTophatMmusculusGRCm38Test extends ReferenceSingleTemplate("tophat") with MmusculusGRCm38
class SingleTophatRnorvegicusRnor_6Test extends ReferenceSingleTemplate("tophat") with RnorvegicusRnor_6
//class SingleTophatCelegansWBcel235Test extends ReferenceSingleTemplate("tophat") with CelegansWBcel235

class SingleGsnapHsapiensGRCh38Test extends ReferenceSingleTemplate("gsnap") with HsapiensGRCh38
class SingleGsnapHsapiensGRCh37Test extends ReferenceSingleTemplate("gsnap") with HsapiensGRCh37
//class SingleGsnapMmusculusGRCm38Test extends ReferenceSingleTemplate("gsnap") with MmusculusGRCm38
class SingleGsnapRnorvegicusRnor_6Test extends ReferenceSingleTemplate("gsnap") with RnorvegicusRnor_6
class SingleGsnapCelegansWBcel235Test extends ReferenceSingleTemplate("gsnap") with CelegansWBcel235

class SingleStarHsapiensGRCh38Test extends ReferenceSingleTemplate("star") with HsapiensGRCh38
class SingleStarHsapiensGRCh37Test extends ReferenceSingleTemplate("star") with HsapiensGRCh37
class SingleStarMmusculusGRCm38Test extends ReferenceSingleTemplate("star") with MmusculusGRCm38
class SingleStarRnorvegicusRnor_6Test extends ReferenceSingleTemplate("star") with RnorvegicusRnor_6
class SingleStarCelegansWBcel235Test extends ReferenceSingleTemplate("star") with CelegansWBcel235

class PairedBwamemHsapiensGRCh38Test extends ReferencePairedTemplate("bwa-mem") with HsapiensGRCh38
class PairedBwamemHsapiensGRCh37Test extends ReferencePairedTemplate("bwa-mem") with HsapiensGRCh37
class PairedBwamemMmusculusGRCm38Test extends ReferencePairedTemplate("bwa-mem") with MmusculusGRCm38
class PairedBwamemRnorvegicusRnor_6Test extends ReferencePairedTemplate("bwa-mem") with RnorvegicusRnor_6
class PairedBwamemCelegansWBcel235Test extends ReferencePairedTemplate("bwa-mem") with CelegansWBcel235

class PairedBowtieHsapiensGRCh38Test extends ReferencePairedTemplate("bowtie") with HsapiensGRCh38
class PairedBowtieHsapiensGRCh37Test extends ReferencePairedTemplate("bowtie") with HsapiensGRCh37
class PairedBowtieMmusculusGRCm38Test extends ReferencePairedTemplate("bowtie") with MmusculusGRCm38
class PairedBowtieRnorvegicusRnor_6Test extends ReferencePairedTemplate("bowtie") with RnorvegicusRnor_6
class PairedBowtieCelegansWBcel235Test extends ReferencePairedTemplate("bowtie") with CelegansWBcel235

class PairedTophatHsapiensGRCh38Test extends ReferencePairedTemplate("tophat") with HsapiensGRCh38
class PairedTophatHsapiensGRCh37Test extends ReferencePairedTemplate("tophat") with HsapiensGRCh37
//class PairedTophatMmusculusGRCm38Test extends ReferencePairedTemplate("tophat") with MmusculusGRCm38
class PairedTophatRnorvegicusRnor_6Test extends ReferencePairedTemplate("tophat") with RnorvegicusRnor_6
//class PairedTophatCelegansWBcel235Test extends ReferencePairedTemplate("tophat") with CelegansWBcel235

class PairedGsnapHsapiensGRCh38Test extends ReferencePairedTemplate("gsnap") with HsapiensGRCh38
class PairedGsnapHsapiensGRCh37Test extends ReferencePairedTemplate("gsnap") with HsapiensGRCh37
//class PairedGsnapMmusculusGRCm38Test extends ReferencePairedTemplate("gsnap") with MmusculusGRCm38
class PairedGsnapRnorvegicusRnor_6Test extends ReferencePairedTemplate("gsnap") with RnorvegicusRnor_6
class PairedGsnapCelegansWBcel235Test extends ReferencePairedTemplate("gsnap") with CelegansWBcel235

class PairedStarHsapiensGRCh38Test extends ReferencePairedTemplate("star") with HsapiensGRCh38
class PairedStarHsapiensGRCh37Test extends ReferencePairedTemplate("star") with HsapiensGRCh37
class PairedStarMmusculusGRCm38Test extends ReferencePairedTemplate("star") with MmusculusGRCm38
class PairedStarRnorvegicusRnor_6Test extends ReferencePairedTemplate("star") with RnorvegicusRnor_6
class PairedStarCelegansWBcel235Test extends ReferencePairedTemplate("star") with CelegansWBcel235

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