package nl.lumc.sasc.biopet.test

import java.io.File

import org.testng.annotations.{ DataProvider, Test }

/**
 * Created by pjvanthof on 04/02/16.
 */
trait Report extends Pipeline {

  addMustHaveFile("report")
  addMustHaveFile("report", "index.html")
  addMustHaveFile("report", "ext")
  addMustHaveFile("report", "ext", "js")
  addMustHaveFile("report", "ext", "js", "sortable.min.js")
  addMustHaveFile("report", "ext", "js", "jquery.min.js")
  addMustHaveFile("report", "ext", "js", "d3.v3.5.5.min.js")
  addMustHaveFile("report", "ext", "js", "bootstrap.min.js")
  addMustHaveFile("report", "ext", "css")
  addMustHaveFile("report", "ext", "css", "bootstrap.min.css")
  addMustHaveFile("report", "ext", "css", "sortable-theme-bootstrap.css")
  addMustHaveFile("report", "ext", "css", "bootstrap-theme.min.css")
  addMustHaveFile("report", "ext", "css", "bootstrap_dashboard.css")
  addMustHaveFile("report", "ext", "fonts")
  addMustHaveFile("report", "ext", "fonts", "glyphicons-halflings-regular.ttf")
  addMustHaveFile("report", "ext", "fonts", "glyphicons-halflings-regular.woff2")
  addMustHaveFile("report", "ext", "fonts", "glyphicons-halflings-regular.woff")
  addMustHaveFile("report", "Versions")
  addMustHaveFile("report", "Versions", "index.html")

}
