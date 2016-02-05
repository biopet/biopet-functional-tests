package nl.lumc.sasc.biopet.test

import java.io.File

import org.testng.annotations.{ DataProvider, Test }

/**
 * Created by pjvanthof on 04/02/16.
 */
trait Report extends Pipeline {

  def reportDir = new File(outputDir, "report")

  private var mustHaveReportFiles: List[File] = Nil
  def addMustHaveReportFile(path: String*) = mustHaveReportFiles ::= new File(reportDir, path.mkString(File.separator))

  @DataProvider(name = "report_must_have_files")
  def reportMustHaveFiles = mustHaveReportFiles.map(Array(_)).toArray

  @Test(dataProvider = "report_must_have_files", dependsOnGroups = Array("parseLog"))
  def testReportMustHaveFile(file: File): Unit = withClue(s"file: $file") {
    file should exist
  }

  private var mustNotHaveReportFiles: List[File] = Nil
  def addMustNotHaveReportFile(path: String*) = mustNotHaveReportFiles ::= new File(reportDir, path.mkString(File.separator))

  @DataProvider(name = "report_must_not_have_files")
  def reportMustNotHaveFiles = mustNotHaveReportFiles.map(Array(_)).toArray

  @Test(dataProvider = "report_must_not_have_files", dependsOnGroups = Array("parseLog"))
  def testReportMustNotHaveFile(file: File): Unit = withClue(s"file: $file") {
    assert(!file.exists())
  }

  addMustHaveReportFile()
  addMustHaveReportFile("index.html")
  addMustHaveReportFile("ext")
  addMustHaveReportFile("ext", "js")
  addMustHaveReportFile("ext", "js", "gears.js")
  addMustHaveReportFile("ext", "js", "sortable.min.js")
  addMustHaveReportFile("ext", "js", "jquery.min.js")
  addMustHaveReportFile("ext", "js", "d3.v3.5.5.min.js")
  addMustHaveReportFile("ext", "js", "bootstrap.min.js")
  addMustHaveReportFile("ext", "css")
  addMustHaveReportFile("ext", "css", "bootstrap.min.css")
  addMustHaveReportFile("ext", "css", "sortable-theme-bootstrap.css")
  addMustHaveReportFile("ext", "css", "bootstrap-theme.min.css")
  addMustHaveReportFile("ext", "css", "bootstrap_dashboard.css")
  addMustHaveReportFile("ext", "fonts")
  addMustHaveReportFile("ext", "fonts", "glyphicons-halflings-regular.ttf")
  addMustHaveReportFile("ext", "fonts", "glyphicons-halflings-regular.woff2")
  addMustHaveReportFile("ext", "fonts", "glyphicons-halflings-regular.woff")

}
