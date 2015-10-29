package nl.lumc.sasc.biopet.test.toucan

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import org.testng.annotations.Test

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

}

class TestPlain extends ToucanSuccess {

  override def outputPath = this.inputVcf map
      { x => x.getAbsolutePath } map
      { x => x.replaceAll(".vcf.gz$", ".vep.normalized.vcf.gz") }
}

class TestWithGoNL extends ToucanSuccess {
  override def goNLFile = Some(Biopet.fixtureFile("toucan" + File.separator +
    "gonl_allchroms_snpindels.sorted.chr.vcf.gz"))

  override def outputPath = this.inputVcf map
     { x => x.getAbsolutePath } map
     { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.vcf.gz") }
}

class TestWithExac extends ToucanSuccess {

  override def exacFile = Some(Biopet.fixtureFile("toucan" + File.separator + "ExAC.r0.3.sites.vep.vcf.gz"))

  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.exac.vcf.gz") }
}

class TestWithGoNLAndExac extends ToucanSuccess {

  override def exacFile = Some(Biopet.fixtureFile("toucan" + File.separator + "ExAC.r0.3.sites.vep.vcf.gz"))
  override def goNLFile = Some(Biopet.fixtureFile("toucan" + File.separator +
    "gonl_allchroms_snpindels.sorted.chr.vcf.gz"))

  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.exac.vcf.gz") }
}
