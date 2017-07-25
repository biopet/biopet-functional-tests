package nl.lumc.sasc.biopet.test.samples

import java.io.File

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

trait Rna3 extends Samples {
  override def samples: Map[String, Set[String]] = addSampleLibrary(super.samples, Rna3.sampleName, "lib1")
  override def configs: List[File] = super.configs :+ Rna1.lib1ConfigFile
}

object Rna3 {
  val sampleName = "rna3"
  def lib1ConfigMap = Map(
    "samples" ->
      Map(
        sampleName ->
          Map(
            "libraries" ->
              Map("lib1" ->
                Map(
                  "R1" -> Biopet
                    .fixtureFile("samples", sampleName, "R1.fq.gz")
                    .getAbsolutePath,
                  "R1_md5" -> "2e53e727dad4cbbfedadeb29404ca0d3",
                  "R2" -> Biopet
                    .fixtureFile("samples", sampleName, "R2.fq.gz")
                    .getAbsolutePath,
                  "R2_md5" -> "2e53e727dad4cbbfedadeb29404ca0d3"
                )))))
  val lib1ConfigFile: File = createTempConfig(lib1ConfigMap, s"$sampleName-lib1")

}
