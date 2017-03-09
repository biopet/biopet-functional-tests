package nl.lumc.sasc.biopet.test

import java.io.File

import com.github.fge.jsonschema.main._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalatest.matchers._
import org.testng.annotations.{ DataProvider, Test }
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb.Implicts._
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb._

import scala.collection.JavaConverters._
import scala.collection.mutable.{ ListBuffer, Map => MutMap }
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source
import scala.util.matching.Regex

/**
 * Created by pjvanthof on 19/09/15.
 */
trait SummaryPipeline extends PipelineSuccess with JValueMatchers {

  implicit val formats = DefaultFormats

  def summaryDbFile = new File(outputDir, pipelineName + ".summary.db")

  private var _summaryDb: SummaryDb = _

  def summaryDb = _summaryDb

  var _runId: Int = _
  def runId = _runId

  @Test(groups = Array("runId"))
  def testRunId: Unit = {
    val runIdFile = new File(outputDir, ".log" + File.separator + "summary.runid")
    runIdFile should exist
    val reader = Source.fromFile(runIdFile)
    _runId = reader.getLines().next().toInt
    reader.close()
  }

  @Test(groups = Array("summary"))
  def testSummaryFileExist(): Unit = assert(summaryDbFile.exists, "Summary file does not exist")

  @Test(groups = Array("summary"), dependsOnGroups = Array("runId"))
  def testOpenSummaryDb: Unit = {
    _summaryDb = SummaryDb.openSqliteSummary(summaryDbFile)
  }

  @Test(dependsOnGroups = Array("summary"))
  def testSummaryRunOutputDir(): Unit = {
    val runs = Await.result(summaryDb.getRuns(runId = Some(runId)), Duration.Inf)
    runs should not be empty
    val run = runs.head
    run.outputDir shouldBe outputDir.getAbsolutePath
  }

  @Test(dependsOnGroups = Array("summary"))
  def testSummaryRunPipelineName(): Unit = {
    val runs = Await.result(summaryDb.getRuns(runId = Some(runId)), Duration.Inf)
    runs should not be empty
    val run = runs.head
    run.name shouldBe pipelineName
  }

  @Test(dependsOnGroups = Array("summary"))
  def testSummaryRunGitHash(): Unit = {
    val runs = Await.result(summaryDb.getRuns(runId = Some(runId)), Duration.Inf)
    runs should not be empty
    val run = runs.head
    run.commitHash should startWith regex """[a-z0-9]{8}"""
    run.commitHash should not endWith "-dirty"
  }

  case class SummaryTest(test: Any => Unit, shouldExist: Boolean = true)

  private var statsTests: MutMap[SummaryGroup, MutMap[List[String], List[SummaryTest]]] = MutMap()

  def addStatsTest(summaryGroup: SummaryGroup,
                   path: List[String] = Nil,
                   test: Any => Unit = _ => {},
                   shouldExist: Boolean = true): Unit = {
    if (!statsTests.contains(summaryGroup)) statsTests += (summaryGroup) -> MutMap()
    statsTests(summaryGroup) += path -> (SummaryTest(test, shouldExist) :: statsTests(summaryGroup).getOrElse(path, Nil))
  }

  @DataProvider(name = "statsTests")
  def summaryStatsProvider() = {
    (for ((group, functions) <- statsTests) yield Array(group, functions)).toArray
  }

  @Test(dataProvider = "statsTests", dependsOnGroups = Array("summary"))
  def testSummaryStats(summaryGroup: SummaryGroup, functions: MutMap[List[String], List[SummaryTest]]): Unit = {
    val statsPaths = functions.keys.map(l => l.mkString("->") -> l).toMap
    val results = summaryDb.getStatKeys(runId, summaryGroup.pipeline, summaryGroup.module.map(ModuleName).getOrElse(NoModule),
      summaryGroup.sample.map(SampleName).getOrElse(NoSample), summaryGroup.library.map(LibraryName).getOrElse(NoLibrary), statsPaths)
    val errors = new ListBuffer[Throwable]
    functions.foreach { x =>
      x._2.foreach { test =>
        try {
          withClue(s"path: ${x._1}, error: ") {
            val value = results(x._1.mkString("->"))
            if (test.shouldExist) {
              require(value.isDefined, "Value does not exist")
              test.test(value.get)
            } else value shouldBe empty
          }
        } catch {
          case s: Throwable => errors += s
        }
      }
    }
    if (errors.nonEmpty) {
      val messages = errors.map { e =>
        e.printStackTrace()
        println()
        e.getMessage
      }
      throw new Exception(s"Error found in summary group: $summaryGroup\n${messages.mkString("\n")}")
    }
  }

  private var settingsTests: MutMap[SummaryGroup, MutMap[List[String], List[Any => Unit]]] = MutMap()

  def addSettingsTest(summaryGroup: SummaryGroup, path: List[String], function: Any => Unit): Unit = {
    if (!settingsTests.contains(summaryGroup)) settingsTests += (summaryGroup) -> MutMap()
    settingsTests(summaryGroup) += path -> (function :: settingsTests(summaryGroup).getOrElse(path, Nil))
  }

  @DataProvider(name = "settingsTests")
  def summarySettingsProvider() = {
    (for ((group, functions) <- settingsTests) yield Array(group, functions)).toArray
  }

  @Test(dataProvider = "settingsTests", dependsOnGroups = Array("summary"))
  def testSummarySettings(summaryGroup: SummaryGroup, functions: MutMap[List[String], List[Any => Unit]]): Unit = {
    val settingsPaths = functions.keys.map(l => l.mkString("->") -> l).toMap
    val results = summaryDb.getSettingKeys(runId, summaryGroup.pipeline, summaryGroup.module.map(ModuleName).getOrElse(NoModule),
      summaryGroup.sample.map(SampleName).getOrElse(NoSample), summaryGroup.library.map(LibraryName).getOrElse(NoLibrary), settingsPaths)
    val errors = new ListBuffer[Throwable]
    functions.foreach { x =>
      x._2.foreach { f =>
        try {
          withClue(s"group: $summaryGroup, path: ${x._1}") {
            val value = results(x._1.mkString("->"))
            require(value.isDefined, "Value does not exist")
            f(value.get)
          }
        } catch {
          case s: Throwable => errors += s
        }
      }
    }
    if (errors.nonEmpty) {
      val messages = errors.map { e =>
        e.printStackTrace()
        println()
        e.getMessage
      }
      throw new Exception(s"Error found in summary group: $summaryGroup\n${messages.mkString("\n")}")
    }
  }

  case class FileTest(group: SummaryGroup,
                      key: String,
                      summaryShouldContain: Boolean = true,
                      fileShouldExist: Option[Boolean] = None,
                      path: Option[File] = None,
                      md5: Option[String] = None)

  private var filesTests: List[FileTest] = Nil

  def addSummaryFileTest(group: SummaryGroup,
                         key: String,
                         summaryShouldContain: Boolean = true,
                         fileShouldExist: Option[Boolean] = None,
                         path: Option[File] = None,
                         md5: Option[String] = None) =
    filesTests :+= FileTest(group, key, summaryShouldContain, fileShouldExist, path, md5)
  def addSummaryFileTest(fileTest: FileTest) = filesTests :+= fileTest

  @DataProvider(name = "SummaryFiles")
  def summaryFilesProvider = {
    filesTests.map(Array(_)).toArray
  }

  @Test(dataProvider = "SummaryFiles", dependsOnGroups = Array("summary"))
  def testSummaryFiles(fileTest: FileTest): Unit = withClue(fileTest) {
    val file = Await.result(summaryDb.getFile(runId, fileTest.group.pipeline,
      fileTest.group.module.map(ModuleName).getOrElse(NoModule),
      fileTest.group.sample.map(SampleName).getOrElse(NoSample),
      fileTest.group.library.map(LibraryName).getOrElse(NoLibrary), fileTest.key), Duration.Inf)
    if (fileTest.summaryShouldContain) {
      file should not be empty
      val f = new File(file.get.path)
      fileTest.path.foreach(file.get.path shouldBe _.getAbsolutePath)
      fileTest.md5.foreach(file.get.md5 shouldBe _)
      fileTest.fileShouldExist.foreach(if (_) f should exist else f should not be exist)
    } else file shouldBe empty
  }

  private var executables: Set[Executable] = Set()

  /** With this method an executable can be added that must exists in the summary */
  def addExecutable(exe: Executable): Unit = executables += exe

  @DataProvider(name = "executables")
  def executablesProvider = executables.map(Array(_)).toArray

  @Test(dataProvider = "executables", dependsOnGroups = Array("summary"))
  def testExecutables(exe: Executable): Unit = withClue(s"Executable: $exe") {
    val exesDb = Await.result(summaryDb.getExecutables(runId = Some(runId), toolName = Some(exe.name)), Duration.Inf)
    require(exesDb.size == 1, "Executable not found in summary")
    val exeDb = exesDb.head
    exeDb.toolName shouldBe exe.name

    exe.version match {
      case Some(r) =>
        require(exeDb.version.isDefined)
        exeDb.version.get should fullyMatch regex r
      case _ => exeDb.version shouldBe empty
    }
  }

  private var notExecutables: Set[String] = Set()

  /** With this method an executable can be added that must not exists in the summary */
  def addNotHavingExecutable(exe: String): Unit = {
    if (notExecutables == null) notExecutables = Set()
    notExecutables += exe
  }

  @DataProvider(name = "notExecutables")
  def notExecutablesProvider = notExecutables.map(Array(_)).toArray

  @Test(dataProvider = "notExecutables", dependsOnGroups = Array("summary"))
  def testNotExecutables(exe: String): Unit = withClue(s"Executable: $exe") {
    val exesDb = Await.result(summaryDb.getExecutables(runId = Some(runId), toolName = Some(exe)), Duration.Inf)
    exesDb shouldBe empty
  }
}

case class SummaryGroup(pipeline: String, module: Option[String] = None,
                        sample: Option[String] = None, library: Option[String] = None)

case class Executable(name: String, version: Option[Regex] = None)

object SummaryPipeline {

  /** Factory for JSON schemas */
  protected val schemaFactory: JsonSchemaFactory = JsonSchemaFactory.byDefault()
}

/** Trait for easier JValue matching. */
trait JValueMatchers {

  private def makeMatchResult(boolTest: => Boolean, obsValue: Any, expValue: Any): MatchResult =
    MatchResult(boolTest,
      s"""Value $obsValue can not be equalized to $expValue""",
      s"""Value $obsValue can be equalized to $expValue""")

  private def makeFileExistsMatchResult(boolTest: => Boolean, obsValue: Any): MatchResult =
    MatchResult(boolTest,
      s"""Value $obsValue can not be checked for file existence.""",
      s"""Value $obsValue exists as a file.""")

  class JValueFileExistMatcher() extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JString(s) => new java.io.File(s).exists()
        case otherwise  => false
      }
      makeFileExistsMatchResult(testFunc, left)
    }
  }

  class JValueIntMatcher(expectedValue: Int) extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JInt(i)     => i == scala.math.BigInt(expectedValue)
        case JDouble(d)  => d == expectedValue
        case JDecimal(d) => d == scala.math.BigDecimal(expectedValue)
        case otherwise   => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  class JValueDoubleMatcher(expectedValue: Double) extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JInt(i)     => i.doubleValue() == expectedValue
        case JDouble(d)  => d == expectedValue
        case JDecimal(d) => d == scala.math.BigDecimal(expectedValue)
        case otherwise   => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  class JValueBoolMatcher(expectedValue: Boolean) extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JBool(i)  => i == expectedValue
        case otherwise => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  class JValueStringMatcher(expectedValue: String) extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JString(s) => s == expectedValue
        case otherwise  => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  class JValueArrayMatcher(expectedValue: List[_]) extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case a: JArray =>
          val values = a.values
          values.size == expectedValue.size && expectedValue.forall(values.contains)
        case otherwise => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  class JValueEmptyMatcher(expectedValue: None.type) extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JNothing  => true
        case otherwise => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  class JValueDoubleRangeMatcher(expectedValue: Double, errorMargin: Double) extends Matcher[JValue] {
    val lowerBound = expectedValue * (1 - errorMargin)
    val higherBound = expectedValue * (1 + errorMargin)
    val outer = this
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JInt(i)    => (lowerBound <= i.doubleValue()) && (i.doubleValue() <= higherBound)
        case JDouble(d) => (lowerBound <= d) && (d <= higherBound)
        //        case JDecimal(d) => lowerBound.<=(scala.math.BigDecimal(expectedValue)) &&
        //          scala.math.BigDecimal(expectedValue).<=(higherBound)
        case otherwise  => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
    def apply(left: Int) = {
      def testFunc: Boolean = (lowerBound <= left.doubleValue()) && (left.doubleValue() <= higherBound)
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  def inInterval(expectedValue: Int, errorMargin: Double) = new JValueDoubleRangeMatcher(expectedValue, errorMargin)
  def inInterval(expectedValue: Double, errorMargin: Double) = new JValueDoubleRangeMatcher(expectedValue, errorMargin)

  def haveValue(expectedValue: List[_]) = new JValueArrayMatcher(expectedValue)
  def haveValue(expectedValue: Boolean) = new JValueBoolMatcher(expectedValue)
  def haveValue(expectedValue: Int) = new JValueIntMatcher(expectedValue)
  def haveValue(expectedValue: Double) = new JValueDoubleMatcher(expectedValue)
  def haveValue(expectedValue: String) = new JValueStringMatcher(expectedValue)
  def haveValue(expectedValue: Option[_]): Matcher[JValue] = expectedValue match {
    case n @ None           => new JValueEmptyMatcher(n)
    case Some(v: Int)       => new JValueIntMatcher(v)
    case Some(v: Double)    => new JValueDoubleMatcher(v)
    case Some(v: String)    => new JValueStringMatcher(v)
    case Some(v: Option[_]) => haveValue(v)
    case otherwise          => throw new RuntimeException(s"Unexpected type for testing JValue: $otherwise")
  }
  def existAsFile = new JValueFileExistMatcher
}
