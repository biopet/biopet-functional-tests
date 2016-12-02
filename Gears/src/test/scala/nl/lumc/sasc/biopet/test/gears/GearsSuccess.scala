package nl.lumc.sasc.biopet.test.gears

import nl.lumc.sasc.biopet.test.MultisampleSuccess

/**
 * Created by pjvan_thof on 2/2/16.
 */
trait GearsSuccess extends Gears with MultisampleSuccess {
  addMustHaveFile("report", "ext", "js", "krona-2.0.js")
  addMustHaveFile("report", "ext", "img")
  addMustHaveFile("report", "ext", "img", "krona")
  addMustHaveFile("report", "ext", "img", "krona", "favicon.ico")
  addMustHaveFile("report", "ext", "img", "krona", "loading.gif")
  addMustHaveFile("report", "ext", "img", "krona", "hidden.png")

  addConditionalFile(gearsUseCentrifuge.getOrElse(true), "report", "Centriguge analysis", "index.html")
  addConditionalFile(gearsUseCentrifuge.getOrElse(true), "report", "Centriguge analysis", "gearscentrifuge-centrifuge_unique_report.html")
  addConditionalFile(gearsUseCentrifuge.getOrElse(true), "report", "Centriguge analysis", "Non-unique", "index.html")
  addConditionalFile(gearsUseCentrifuge.getOrElse(true), "report", "Centriguge analysis", "Non-unique", "gearscentrifuge-centrifuge_report.html")

  addConditionalFile(gearsUseKraken.getOrElse(false), "report", "Kraken analysis", "index.html")

  addConditionalFile(gearUseQiimeOpen.getOrElse(false), "report", "Qiime open reference analysis", "index.html")
  addConditionalFile(gearUseQiimeClosed.getOrElse(false), "report", "Qiime closed reference analysis", "index.html")
}
