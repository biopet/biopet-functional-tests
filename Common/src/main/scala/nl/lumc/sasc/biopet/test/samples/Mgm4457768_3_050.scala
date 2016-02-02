package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

trait Mgm4457768_3_050 extends Samples with Rna2Lib1

trait Mgm4457768_3_050Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Mgm4457768_3_050.sampleName, "lib1")
  override def configs = super.configs :+ Rna2.lib1ConfigFile
}

object Mgm4457768_3_050 {
  val sampleName = "Mgm4457768_3_050"
  val lib1ConfigMap = Map("samples" ->
    Map(s"$sampleName" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "mgm4457768.3.050.R1.fastq.gz").getAbsolutePath,
            "R1_md5" -> "7f8707a1a1d61f53aca45e602962da4a",
            "R2" -> Biopet.fixtureFile("samples", sampleName, "mgm4457768.3.050.R2.fastq.gz").getAbsolutePath,
            "R2_md5" -> "0693a1a908494bbe32a007c863a64461"
          )
        )
      )
    )
  )
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, s"$sampleName-lib1")
}

