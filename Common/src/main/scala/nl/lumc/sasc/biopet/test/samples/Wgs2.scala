package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Biopet
import nl.lumc.sasc.biopet.test.utils._

/**
 * Created by pjvan_thof on 11/16/15.
 */
trait Wgs2 extends Samples with Wgs2Lib1 with Wgs2Lib2

trait Wgs2Lib1 extends Samples {
  override def samples = addSampleLibrary(super.samples, "wgs2", "lib1")
  override def configs = super.configs :+ Wgs2.lib1ConfigFile
}

trait Wgs2Lib2 extends Samples {
  override def samples = addSampleLibrary(super.samples, "wgs2", "lib2")
  override def configs = super.configs :+ Wgs2.lib2ConfigFile
}

object Wgs2 {
  val lib1ConfigMap = Map("samples" ->
    Map("wgs2" ->
      Map("libraries" ->
        Map("lib1" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", "wgs2", "wgs2-lib1_R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "6fb02af910026041f9ea76cd28968732",
            "R2" -> Biopet.fixtureFile("samples", "wgs2", "wgs2-lib1_R2.fq.gz").getAbsolutePath,
            "R2_md5" -> "537ffc52342314d839e7fdd91bbdccd0"
          )
        )
      )
    )
  )
  val lib2ConfigMap = Map("samples" ->
    Map("wgs2" ->
      Map("libraries" ->
        Map("lib2" ->
          Map(
            "R1" -> Biopet.fixtureFile("samples", "wgs2", "wgs2-lib2_R1.fq.gz").getAbsolutePath,
            "R1_md5" -> "df64e84fdc9a2d7a9301f2aac0071aee",
            "R2" -> Biopet.fixtureFile("samples", "wgs2", "wgs2-lib2_R2.fq.gz").getAbsolutePath,
            "R2_md5" -> "47a65ad648ac08e802c07669629054ea"
          )
        )
      )
    )
  )

  val lib1ConfigFile = createTempConfig(lib1ConfigMap, "wgs2-lib1")
  val lib2ConfigFile = createTempConfig(lib2ConfigMap, "wgs2-lib2")
}
