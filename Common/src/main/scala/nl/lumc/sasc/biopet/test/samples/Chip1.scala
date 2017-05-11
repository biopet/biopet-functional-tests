package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

/**
  * Created by pjvan_thof on 27-5-16.
  */
trait Chip1 extends Samples with Chip1Lib1 with Chip1LibControl

trait Chip1Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Chip1.sampleName, "lib1")
  override def configs = super.configs :+ Chip1.lib1ConfigFile
}

trait Chip1LibControl extends Samples {
  override def samples = addSampleLibrary(super.samples, Chip1.sampleName, "libControl")
  override def configs = super.configs :+ Chip1.libControlConfigFile
}

object Chip1 {
  val sampleName = "chip1"
  val lib1ConfigMap = Map(
    "samples" ->
      Map(
        s"$sampleName" ->
          Map(
            "libraries" ->
              Map("lib1" ->
                Map(
                  "R1" -> Biopet
                    .fixtureFile("samples", sampleName, "sample1_R1.fastq.gz")
                    .getAbsolutePath,
                  "R1_md5" -> "d538c6e674b7152cf201a10dd75f08e8",
                  "R2" -> Biopet
                    .fixtureFile("samples", sampleName, "sample1_R2.fastq.gz")
                    .getAbsolutePath,
                  "R2_md5" -> "9b662817e93a625642e312f595c1d7e0"
                )))))
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, s"$sampleName-lib1")

  val libControlConfigMap = Map(
    "samples" ->
      Map(
        s"$sampleName" ->
          Map(
            "libraries" ->
              Map("libControl" ->
                Map(
                  "R1" -> Biopet
                    .fixtureFile("samples", sampleName, "sampleCon_R1.fastq.gz")
                    .getAbsolutePath,
                  "R1_md5" -> "8e475aa9239be5188f4850e989765dc3",
                  "R2" -> Biopet
                    .fixtureFile("samples", sampleName, "sampleCon_R2.fastq.gz")
                    .getAbsolutePath,
                  "R2_md5" -> "a51c245c4e8790695d5299728e07360e"
                )))))
  val libControlConfigFile = createTempConfig(libControlConfigMap, s"$sampleName-libControl")

}
