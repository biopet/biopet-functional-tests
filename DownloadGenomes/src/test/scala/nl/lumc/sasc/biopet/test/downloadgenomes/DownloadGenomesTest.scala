package nl.lumc.sasc.biopet.test.downloadgenomes

import java.io.{ File, PrintWriter }

/**
 * Created by pjvan_thof on 13-5-16.
 */
class EcoliTest extends DownloadGenomes {
  val assemblyReport = new File(resourcePath("/GCF_000005845.2.assembly.report.txt"))
  val config = File.createTempFile("config.", ".yml")
  config.deleteOnExit()
  private val writer = new PrintWriter(config)
  writer.println(
    s"""
      |E.coli:
      |  K-12_MG1655:
      |    ncbi_assembly_report: ${assemblyReport.getAbsolutePath}
    """.stripMargin)
  writer.close()

  override def referenceConfigs = config :: super.referenceConfigs
}

class CelegansTest extends DownloadGenomes {
  override def referenceConfigs = new File(resourcePath("/C.elegans_WBcel235.yml")) :: super.referenceConfigs
}
