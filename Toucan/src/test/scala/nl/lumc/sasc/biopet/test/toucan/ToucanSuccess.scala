package nl.lumc.sasc.biopet.test.toucan

import java.io.File

import org.testng.annotations.Test

/**
 * Created by ahbbollen on 22-10-15.
 */
trait ToucanSuccess extends Toucan {

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
  //TODO: set this!
  //override def exacFile = Some(new File("path/to/file.vcf.gz"))
  //override def goNLFile = Some(new File("path/to/file.vcf.gz"))

  override def outputPath = this.inputVcf map
     { x => x.getAbsolutePath } map
     { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.vcf.gz") }
}

class TestWithExac extends ToucanSuccess {

  //TODO: set this!
  //override def exacFile = Some(new File("path/to/file.vcf.gz"))

  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.exac.vcf.gz") }
}

class TestWithGoNLAndExac extends ToucanSuccess {

  //TODO: set this!
  //override def exacFile = Some(new File("path/to/file.vcf.gz"))
  //override def goNLFile = Some(new File("path/to/file.vcf.gz"))
  override def outputPath = this.inputVcf map
    { x => x.getAbsolutePath } map
    { x => x.replaceAll(".vcf.gz$", ".vep.normalized.gonl.exac.vcf.gz") }
}
