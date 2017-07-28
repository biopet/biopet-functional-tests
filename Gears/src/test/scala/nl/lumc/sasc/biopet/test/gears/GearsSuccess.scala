package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.MultisampleSuccess

/**
  * Created by pjvan_thof on 2/2/16.
  */
trait GearsSuccess extends Gears with MultisampleSuccess {
  def paired: Boolean

  override def shouldHaveLibs: Boolean = !skipFlexiprep.getOrElse(false)

  addMustHaveFile("report", "ext", "js", "krona-2.0.js")
  addMustHaveFile("report", "ext", "img")
  addMustHaveFile("report", "ext", "img", "krona")
  addMustHaveFile("report", "ext", "img", "krona", "favicon.ico")
  addMustHaveFile("report", "ext", "img", "krona", "loading.gif")
  addMustHaveFile("report", "ext", "img", "krona", "hidden.png")

  addConditionalFile(gearsUseCentrifuge.getOrElse(true),
                     "report",
                     "Centrifuge analysis",
                     "index.html")
  addConditionalFile(gearsUseCentrifuge.getOrElse(true),
                     "report",
                     "Centrifuge analysis",
                     "gearscentrifuge-centrifuge_unique_report.html")
  addConditionalFile(gearsUseCentrifuge.getOrElse(true),
                     "report",
                     "Centrifuge analysis",
                     "Non-unique",
                     "index.html")
  addConditionalFile(gearsUseCentrifuge.getOrElse(true),
                     "report",
                     "Centrifuge analysis",
                     "Non-unique",
                     "gearscentrifuge-centrifuge_report.html")

  addConditionalFile(gearsUseKraken.getOrElse(false), "report", "Kraken analysis", "index.html")

  addConditionalFile(gearUseQiimeOpen.getOrElse(false),
                     "report",
                     "Qiime open reference analysis",
                     "index.html")
  addConditionalFile(gearUseQiimeClosed.getOrElse(false),
                     "report",
                     "Qiime closed reference analysis",
                     "index.html")

  samples.foreach {
    case (sample, libraries) =>
      addConditionalFile(gearsUseCentrifuge.getOrElse(true),
                         "report",
                         "Samples",
                         sample,
                         "Centrifuge analysis",
                         "index.html")
      addConditionalFile(gearsUseKraken.getOrElse(false),
                         "report",
                         "Samples",
                         sample,
                         "Kraken analysis",
                         "index.html")
      addConditionalFile(gearUseQiimeOpen.getOrElse(false),
                         "report",
                         "Samples",
                         sample,
                         "Qiime open reference analysis",
                         "index.html")
      addConditionalFile(gearUseQiimeClosed.getOrElse(false),
                         "report",
                         "Samples",
                         sample,
                         "Qiime closed reference analysis",
                         "index.html")

      addConditionalFile(gearsUseCentrifuge.getOrElse(true), "samples", sample, "centrifuge")
      addConditionalFile(gearsUseKraken.getOrElse(false), "samples", sample, "kraken")
      addConditionalFile(gearUseQiimeOpen.getOrElse(false), "samples", sample, "qiime_open")
      addConditionalFile(gearUseQiimeClosed.getOrElse(false), "samples", sample, "qiime_closed")

      addConditionalFile(
        (gearUseQiimeOpen.getOrElse(false) || gearUseQiimeClosed.getOrElse(false)) && paired,
        "samples",
        sample,
        "combine_reads",
        "flash")
      addMustNotHaveFile("samples", sample, "combine_reads", "flash", "out.extendedFrags.fastq.gz")
      addMustNotHaveFile("samples", sample, "combine_reads", "flash", "out.notCombined_1.fastq.gz")
      addMustNotHaveFile("samples", sample, "combine_reads", "flash", "out.notCombined_2.fastq.gz")
      addConditionalFile(
        (gearUseQiimeOpen.getOrElse(false) || gearUseQiimeClosed.getOrElse(false)) && paired,
        "samples",
        sample,
        "combine_reads",
        "flash",
        ".out.extendedFrags.fastq.gz.Flash.out"
      )

      addMustNotHaveFile("samples", sample, "qiime_open", "split_libraries_fastq", "seqs.fna")
      addMustNotHaveFile("samples", sample, "qiime_closed", "split_libraries_fastq", "seqs.fna")

      libraries.foreach { library =>
        //TODO: library tests
      }
  }

}
