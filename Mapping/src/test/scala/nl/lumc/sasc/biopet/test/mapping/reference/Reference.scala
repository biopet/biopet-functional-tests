package nl.lumc.sasc.biopet.test.mapping.reference

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.aligners._
import nl.lumc.sasc.biopet.test.references
import nl.lumc.sasc.biopet.test.mapping.{Mapping, MappingSuccess}

/**
  * Created by pjvanthof on 06/08/15.
  */
trait ReferenceSingleTemplate extends MappingSuccess {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  // we do a R1 only alignment, so enforce paired to be disabled
  override def paired = false
}

trait ReferencePairedTemplate extends MappingSuccess {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
}

trait HsapiensGRCh38 extends Mapping with references.HsapiensGRCh38 {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait HsapiensGRCh37 extends Mapping with references.HsapiensGRCh37 {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait HsapiensHg19 extends Mapping with references.HsapiensHg19 {
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait MmusculusGRCm38 extends Mapping with references.MmusculusGRCm38 {
  //TODO: M.musculus specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait RnorvegicusRnor_6 extends Mapping with references.RnorvegicusRnor_6 {
  //TODO: R.norvegicus specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait CelegansWBcel235 extends Mapping with references.CelegansWBcel235 {
  //TODO: C.elegans specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait ClfamiliarisCanFam3_1 extends Mapping with references.ClfamiliarisCanFam3_1 {
  //TODO: M.musculus specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait DrerioGRCz10 extends Mapping with references.DrerioGRCz10 {
  //TODO: M.musculus specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

trait DmelanogasterBDGP6 extends Mapping with references.DmelanogasterBDGP6 {
  //TODO: M.musculus specific files
  override def r1 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("flexiprep" + File.separator + "ct_r2.fq.gz"))
}

class SingleBwamemHsapiensGRCh38Test
    extends ReferenceSingleTemplate
    with BwaMem
    with HsapiensGRCh38
class SingleBwamemHsapiensGRCh37Test
    extends ReferenceSingleTemplate
    with BwaMem
    with HsapiensGRCh37
class SingleBwamemHsapiensHg19Test extends ReferenceSingleTemplate with BwaMem with HsapiensHg19
class SingleBwamemMmusculusGRCm38Test
    extends ReferenceSingleTemplate
    with BwaMem
    with MmusculusGRCm38
class SingleBwamemRnorvegicusRnor_6Test
    extends ReferenceSingleTemplate
    with BwaMem
    with RnorvegicusRnor_6
class SingleBwamemCelegansWBcel235Test
    extends ReferenceSingleTemplate
    with BwaMem
    with CelegansWBcel235
class SingleBwamemClfamiliarisCanFam3_1Test
    extends ReferenceSingleTemplate
    with BwaMem
    with ClfamiliarisCanFam3_1
class SingleBwamemDrerioGRCz10Test extends ReferenceSingleTemplate with BwaMem with DrerioGRCz10
class SingleBwamemDmelanogasterBDGP6Test
    extends ReferenceSingleTemplate
    with BwaMem
    with DmelanogasterBDGP6

class SingleBowtieHsapiensGRCh38Test
    extends ReferenceSingleTemplate
    with Bowtie
    with HsapiensGRCh38
class SingleBowtieHsapiensGRCh37Test
    extends ReferenceSingleTemplate
    with Bowtie
    with HsapiensGRCh37
class SingleBowtieHsapiensHg19Test extends ReferenceSingleTemplate with Bowtie with HsapiensHg19
class SingleBowtieMmusculusGRCm38Test
    extends ReferenceSingleTemplate
    with Bowtie
    with MmusculusGRCm38
class SingleBowtieRnorvegicusRnor_6Test
    extends ReferenceSingleTemplate
    with Bowtie
    with RnorvegicusRnor_6
class SingleBowtieCelegansWBcel235Test
    extends ReferenceSingleTemplate
    with Bowtie
    with CelegansWBcel235
class SingleBowtieClfamiliarisCanFam3_1Test
    extends ReferenceSingleTemplate
    with Bowtie
    with ClfamiliarisCanFam3_1
class SingleBowtieDrerioGRCz10Test extends ReferenceSingleTemplate with Bowtie with DrerioGRCz10
class SingleBowtieDmelanogasterBDGP6Test
    extends ReferenceSingleTemplate
    with Bowtie
    with DmelanogasterBDGP6

class SingleBowtie2HsapiensGRCh38Test
    extends ReferenceSingleTemplate
    with Bowtie2
    with HsapiensGRCh38
class SingleBowtie2HsapiensGRCh37Test
    extends ReferenceSingleTemplate
    with Bowtie2
    with HsapiensGRCh37
class SingleBowtie2HsapiensHg19Test extends ReferenceSingleTemplate with Bowtie2 with HsapiensHg19
class SingleBowtie2MmusculusGRCm38Test
    extends ReferenceSingleTemplate
    with Bowtie2
    with MmusculusGRCm38
class SingleBowtie2RnorvegicusRnor_6Test
    extends ReferenceSingleTemplate
    with Bowtie2
    with RnorvegicusRnor_6
class SingleBowtie2CelegansWBcel235Test
    extends ReferenceSingleTemplate
    with Bowtie2
    with CelegansWBcel235
class SingleBowtie2ClfamiliarisCanFam3_1Test
    extends ReferenceSingleTemplate
    with Bowtie2
    with ClfamiliarisCanFam3_1
class SingleBowtie2DrerioGRCz10Test extends ReferenceSingleTemplate with Bowtie2 with DrerioGRCz10
class SingleBowtie2DmelanogasterBDGP6Test
    extends ReferenceSingleTemplate
    with Bowtie2
    with DmelanogasterBDGP6

class SingleTophatHsapiensGRCh38Test
    extends ReferenceSingleTemplate
    with Tophat
    with HsapiensGRCh38
class SingleTophatHsapiensGRCh37Test
    extends ReferenceSingleTemplate
    with Tophat
    with HsapiensGRCh37
class SingleTophatHsapiensHg19Test extends ReferenceSingleTemplate with Tophat with HsapiensHg19
class SingleTophatMmusculusGRCm38Test
    extends ReferenceSingleTemplate
    with Tophat
    with MmusculusGRCm38
class SingleTophatRnorvegicusRnor_6Test
    extends ReferenceSingleTemplate
    with Tophat
    with RnorvegicusRnor_6
//class SingleTophatCelegansWBcel235Test extends ReferenceSingleTemplate with Tophat with CelegansWBcel235
class SingleTophatClfamiliarisCanFam3_1Test
    extends ReferenceSingleTemplate
    with Tophat
    with ClfamiliarisCanFam3_1
//class SingleTophatDrerioGRCz10Test extends ReferenceSingleTemplate with Tophat with DrerioGRCz10
//class SingleTophatDmelanogasterBDGP6Test extends ReferenceSingleTemplate with Tophat with DmelanogasterBDGP6

class SingleGsnapHsapiensGRCh38Test extends ReferenceSingleTemplate with Gsnap with HsapiensGRCh38
class SingleGsnapHsapiensGRCh37Test extends ReferenceSingleTemplate with Gsnap with HsapiensGRCh37
class SingleGsnapHsapiensHg19Test extends ReferenceSingleTemplate with Gsnap with HsapiensHg19
class SingleGsnapMmusculusGRCm38Test
    extends ReferenceSingleTemplate
    with Gsnap
    with MmusculusGRCm38
class SingleGsnapRnorvegicusRnor_6Test
    extends ReferenceSingleTemplate
    with Gsnap
    with RnorvegicusRnor_6
class SingleGsnapCelegansWBcel235Test
    extends ReferenceSingleTemplate
    with Gsnap
    with CelegansWBcel235
class SingleGsnapClfamiliarisCanFam3_1Test
    extends ReferenceSingleTemplate
    with Gsnap
    with ClfamiliarisCanFam3_1
class SingleGsnapDrerioGRCz10Test extends ReferenceSingleTemplate with Gsnap with DrerioGRCz10
class SingleGsnapDmelanogasterBDGP6Test
    extends ReferenceSingleTemplate
    with Gsnap
    with DmelanogasterBDGP6

class SingleStarHsapiensGRCh38Test extends ReferenceSingleTemplate with Star with HsapiensGRCh38
class SingleStarHsapiensGRCh37Test extends ReferenceSingleTemplate with Star with HsapiensGRCh37
class SingleStarHsapiensHg19Test extends ReferenceSingleTemplate with Star with HsapiensHg19
class SingleStarMmusculusGRCm38Test extends ReferenceSingleTemplate with Star with MmusculusGRCm38
class SingleStarRnorvegicusRnor_6Test
    extends ReferenceSingleTemplate
    with Star
    with RnorvegicusRnor_6
class SingleStarCelegansWBcel235Test
    extends ReferenceSingleTemplate
    with Star
    with CelegansWBcel235
class SingleStarClfamiliarisCanFam3_1Test
    extends ReferenceSingleTemplate
    with Star
    with ClfamiliarisCanFam3_1
class SingleStarDrerioGRCz10Test extends ReferenceSingleTemplate with Star with DrerioGRCz10
class SingleStarDmelanogasterBDGP6Test
    extends ReferenceSingleTemplate
    with Star
    with DmelanogasterBDGP6

//class SingleHistat2HsapiensGRCh38Test extends ReferenceSingleTemplate with Hisat2 with HsapiensGRCh38
//class SingleHistat2HsapiensGRCh37Test extends ReferenceSingleTemplate with Hisat2 with HsapiensGRCh37
//class SingleHistat2MmusculusGRCm38Test extends ReferenceSingleTemplate with Hisat2 with MmusculusGRCm38
//class SingleHistat2RnorvegicusRnor_6Test extends ReferenceSingleTemplate with Hisat2 with RnorvegicusRnor_6
//class SingleHistat2CelegansWBcel235Test extends ReferenceSingleTemplate with Hisat2 with CelegansWBcel235

class PairedBwamemHsapiensGRCh38Test
    extends ReferencePairedTemplate
    with BwaMem
    with HsapiensGRCh38
class PairedBwamemHsapiensGRCh37Test
    extends ReferencePairedTemplate
    with BwaMem
    with HsapiensGRCh37
class PairedBwamemHsapiensHg19Test extends ReferencePairedTemplate with BwaMem with HsapiensHg19
class PairedBwamemMmusculusGRCm38Test
    extends ReferencePairedTemplate
    with BwaMem
    with MmusculusGRCm38
class PairedBwamemRnorvegicusRnor_6Test
    extends ReferencePairedTemplate
    with BwaMem
    with RnorvegicusRnor_6
class PairedBwamemCelegansWBcel235Test
    extends ReferencePairedTemplate
    with BwaMem
    with CelegansWBcel235
class PairedBwamemClfamiliarisCanFam3_1Test
    extends ReferencePairedTemplate
    with BwaMem
    with ClfamiliarisCanFam3_1
class PairedBwamemDrerioGRCz10Test extends ReferencePairedTemplate with BwaMem with DrerioGRCz10
class PairedBwamemDmelanogasterBDGP6Test
    extends ReferencePairedTemplate
    with BwaMem
    with DmelanogasterBDGP6

class PairedBowtieHsapiensGRCh38Test
    extends ReferencePairedTemplate
    with Bowtie
    with HsapiensGRCh38
class PairedBowtieHsapiensGRCh37Test
    extends ReferencePairedTemplate
    with Bowtie
    with HsapiensGRCh37
class PairedBowtieHsapiensHg19Test extends ReferencePairedTemplate with Bowtie with HsapiensHg19
class PairedBowtieMmusculusGRCm38Test
    extends ReferencePairedTemplate
    with Bowtie
    with MmusculusGRCm38
class PairedBowtieRnorvegicusRnor_6Test
    extends ReferencePairedTemplate
    with Bowtie
    with RnorvegicusRnor_6
class PairedBowtieCelegansWBcel235Test
    extends ReferencePairedTemplate
    with Bowtie
    with CelegansWBcel235
class PairedBowtieClfamiliarisCanFam3_1Test
    extends ReferencePairedTemplate
    with Bowtie
    with ClfamiliarisCanFam3_1
class PairedBowtieDrerioGRCz10Test extends ReferencePairedTemplate with Bowtie with DrerioGRCz10
class PairedBowtieDmelanogasterBDGP6Test
    extends ReferencePairedTemplate
    with Bowtie
    with DmelanogasterBDGP6

class PairedBowtie2HsapiensGRCh38Test
    extends ReferencePairedTemplate
    with Bowtie2
    with HsapiensGRCh38
class PairedBowtie2HsapiensGRCh37Test
    extends ReferencePairedTemplate
    with Bowtie2
    with HsapiensGRCh37
class PairedBowtie2HsapiensHg19Test extends ReferencePairedTemplate with Bowtie2 with HsapiensHg19
class PairedBowtie2MmusculusGRCm38Test
    extends ReferencePairedTemplate
    with Bowtie2
    with MmusculusGRCm38
class PairedBowtie2RnorvegicusRnor_6Test
    extends ReferencePairedTemplate
    with Bowtie2
    with RnorvegicusRnor_6
class PairedBowtie2CelegansWBcel235Test
    extends ReferencePairedTemplate
    with Bowtie2
    with CelegansWBcel235
class PairedBowtie2ClfamiliarisCanFam3_1Test
    extends ReferencePairedTemplate
    with Bowtie2
    with ClfamiliarisCanFam3_1
class PairedBowtie2DrerioGRCz10Test extends ReferencePairedTemplate with Bowtie2 with DrerioGRCz10
class PairedBowtie2DmelanogasterBDGP6Test
    extends ReferencePairedTemplate
    with Bowtie2
    with DmelanogasterBDGP6

class PairedTophatHsapiensGRCh38Test
    extends ReferencePairedTemplate
    with Tophat
    with HsapiensGRCh38
class PairedTophatHsapiensGRCh37Test
    extends ReferencePairedTemplate
    with Tophat
    with HsapiensGRCh37
class PairedTophatHsapiensHg19Test extends ReferencePairedTemplate with Tophat with HsapiensHg19
//class PairedTophatMmusculusGRCm38Test extends ReferencePairedTemplate with Tophat with MmusculusGRCm38
//class PairedTophatRnorvegicusRnor_6Test extends ReferencePairedTemplate with Tophat with RnorvegicusRnor_6
//class PairedTophatCelegansWBcel235Test extends ReferencePairedTemplate with Tophat with CelegansWBcel235
class PairedTophatClfamiliarisCanFam3_1Test
    extends ReferencePairedTemplate
    with Tophat
    with ClfamiliarisCanFam3_1
//class PairedTophatDrerioGRCz10Test extends ReferencePairedTemplate with Tophat with DrerioGRCz10
//class PairedTophatDmelanogasterBDGP6Test extends ReferencePairedTemplate with Tophat with DmelanogasterBDGP6

class PairedGsnapHsapiensGRCh38Test extends ReferencePairedTemplate with Gsnap with HsapiensGRCh38
class PairedGsnapHsapiensGRCh37Test extends ReferencePairedTemplate with Gsnap with HsapiensGRCh37
class PairedGsnapHsapiensHg19Test extends ReferencePairedTemplate with Gsnap with HsapiensHg19
class PairedGsnapMmusculusGRCm38Test
    extends ReferencePairedTemplate
    with Gsnap
    with MmusculusGRCm38
class PairedGsnapRnorvegicusRnor_6Test
    extends ReferencePairedTemplate
    with Gsnap
    with RnorvegicusRnor_6
class PairedGsnapCelegansWBcel235Test
    extends ReferencePairedTemplate
    with Gsnap
    with CelegansWBcel235
class PairedGsnapClfamiliarisCanFam3_1Test
    extends ReferencePairedTemplate
    with Gsnap
    with ClfamiliarisCanFam3_1
class PairedGsnapDrerioGRCz10Test extends ReferencePairedTemplate with Gsnap with DrerioGRCz10
class PairedGsnapDmelanogasterBDGP6Test
    extends ReferencePairedTemplate
    with Gsnap
    with DmelanogasterBDGP6

class PairedStarHsapiensGRCh38Test extends ReferencePairedTemplate with Star with HsapiensGRCh38
class PairedStarHsapiensGRCh37Test extends ReferencePairedTemplate with Star with HsapiensGRCh37
class PairedStarHsapiensHg19Test extends ReferencePairedTemplate with Star with HsapiensHg19
class PairedStarMmusculusGRCm38Test extends ReferencePairedTemplate with Star with MmusculusGRCm38
class PairedStarRnorvegicusRnor_6Test
    extends ReferencePairedTemplate
    with Star
    with RnorvegicusRnor_6
class PairedStarCelegansWBcel235Test
    extends ReferencePairedTemplate
    with Star
    with CelegansWBcel235
class PairedStarClfamiliarisCanFam3_1Test
    extends ReferencePairedTemplate
    with Star
    with ClfamiliarisCanFam3_1
class PairedStarDrerioGRCz10Test extends ReferencePairedTemplate with Star with DrerioGRCz10
class PairedStarDmelanogasterBDGP6Test
    extends ReferencePairedTemplate
    with Star
    with DmelanogasterBDGP6

//class PairedHistat2HsapiensGRCh38Test extends ReferencePairedTemplate with Hisat2 with HsapiensGRCh38
//class PairedHistat2HsapiensGRCh37Test extends ReferencePairedTemplate with Hisat2 with HsapiensGRCh37
//class PairedHistat2MmusculusGRCm38Test extends ReferencePairedTemplate with Hisat2 with MmusculusGRCm38
//class PairedHistat2RnorvegicusRnor_6Test extends ReferencePairedTemplate with Hisat2 with RnorvegicusRnor_6
//class PairedHistat2CelegansWBcel235Test extends ReferencePairedTemplate with Hisat2 with CelegansWBcel235
