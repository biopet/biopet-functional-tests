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
}
