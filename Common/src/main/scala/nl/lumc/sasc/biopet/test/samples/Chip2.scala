package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 27-5-16.
 */
trait Chip2 extends Samples with Chip2Lib1 with Chip2LibControl

trait Chip2Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, Chip2.sampleName, "lib1")
  override def configs = super.configs :+ Chip2.lib1ConfigFile
}

trait Chip2LibControl extends Samples {
  override def samples = addSampleLibrary(super.samples, Chip2.sampleName, "libControl")
  override def configs = super.configs :+ Chip2.libControlConfigFile
}

object Chip2 {
  val sampleName = "chip2"
  val lib1ConfigMap = Map("samples" ->
    Map(s"$sampleName" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "sample2_R1.fastq.gz").getAbsolutePath,
            "R1_md5" -> "cf07fa018c049a0ca97f91f0e5b958a2",
            "R2" -> Biopet.fixtureFile("samples", sampleName, "sample2_R2.fastq.gz").getAbsolutePath,
            "R2_md5" -> "74d30ded0d64bcb60ccbff3a390453b4"
          )
        )
      )
    )
  )
  val lib1ConfigFile = createTempConfig(lib1ConfigMap, s"$sampleName-lib1")

  val libControlConfigMap = Map("samples" ->
    Map(s"$sampleName" ->
      Map("libraries" ->
        Map("libControl" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", sampleName, "sampleCon_R1.fastq.gz").getAbsolutePath,
            "R1_md5" -> "8e475aa9239be5188f4850e989765dc3",
            "R2" -> Biopet.fixtureFile("samples", sampleName, "sampleCon_R2.fastq.gz").getAbsolutePath,
            "R2_md5" -> "a51c245c4e8790695d5299728e07360e"
          )
        )
      )
    )
  )
  val libControlConfigFile = createTempConfig(libControlConfigMap, s"$sampleName-libControl")

}

