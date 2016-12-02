package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.MultisampleSuccess

/**
 * Created by pjvan_thof on 2/2/16.
 */
trait GearsSuccess extends Gears with MultisampleSuccess {
  addMustHaveReportFile("ext", "js", "krona-2.0.js")
  addMustHaveReportFile("ext", "img")
  addMustHaveReportFile("ext", "img", "krona")
  addMustHaveReportFile("ext", "img", "krona", "favicon.ico")
  addMustHaveReportFile("ext", "img", "krona", "loading.gif")
  addMustHaveReportFile("ext", "img", "krona", "hidden.png")

  addConditionalReportFile(gearsUseCentrifuge.getOrElse(true), "Centriguge analysis", "index.html")
  addConditionalReportFile(gearsUseCentrifuge.getOrElse(true), "Centriguge analysis", "gearscentrifuge-centrifuge_unique_report.html")
  addConditionalReportFile(gearsUseCentrifuge.getOrElse(true), "Centriguge analysis", "Non-unique", "index.html")
  addConditionalReportFile(gearsUseCentrifuge.getOrElse(true), "Centriguge analysis", "Non-unique", "gearscentrifuge-centrifuge_report.html")

  addConditionalReportFile(gearsUseKraken.getOrElse(false), "Kraken analysis", "index.html")

  addConditionalReportFile(gearUseQiimeOpen.getOrElse(false), "Qiime open reference analysis", "index.html")
  addConditionalReportFile(gearUseQiimeClosed.getOrElse(false), "Qiime closed reference analysis", "index.html")
}
