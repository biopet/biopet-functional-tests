package nl.lumc.sasc.biopet.test

import com.github.fge.jsonschema.main._

import java.io.File

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.scalatest.matchers._
import org.testng.annotations.{ DataProvider, Test }

import scala.collection.mutable.{ Map => MutMap }
import scala.collection.JavaConverters._
import scala.util.matching.Regex

/**
 * Created by pjvanthof on 19/09/15.
 */
trait SummaryPipeline extends PipelineSuccess with JValueMatchers {

  implicit val formats = DefaultFormats

  def summaryFile: File

  /** URL to summary schema resource file, if defined. */
  def summarySchemaUrl: Option[String] = None

  private lazy val summarySchema = summarySchemaUrl.map { url => parse(getClass.getResourceAsStream(url)) }

  final protected lazy val schema: Option[JsonSchema] = summarySchema
    .map { jv => SummaryPipeline.schemaFactory.getJsonSchema(asJsonNode(jv)) }

  private var _summary: JValue = _

  /** This will return the parsed summary, this method only work when the group "parseSummary" is done */
  def summary = _summary

  type SummaryTestFunc = JValue => Unit

  private val summaryTests: MutMap[Seq[String], Seq[SummaryTestFunc]] = MutMap()

  def addSummaryTest(pathTokens: Seq[String], testFuncs: Seq[JValue => Unit]): Unit =
    summaryTests(pathTokens) = summaryTests.getOrElse(pathTokens, Seq()) ++ testFuncs

  @Test(groups = Array("parseSummary"))
  def parseSummary(): Unit = _summary = parse(summaryFile)

  @Test(groups = Array("parseSummary"))
  def testSummarySchema(): Unit =
    if (summarySchemaUrl.isDefined) {
      schema shouldBe defined
      schema.get.validate(asJsonNode(summary)).iterator().asScala.toSeq should have length 0
    }

  @DataProvider(name = "summaryTests")
  def summaryTestsProvider() = {
    (for {
      (pathTokens, testFuncs) <- summaryTests
      testFunc <- testFuncs
    } yield Array(pathTokens, pathTokens.foldLeft(summary) { case (curjv, p) => curjv \ p }, testFunc)).toArray
  }

  @Test(dataProvider = "summaryTests", dependsOnGroups = Array("parseSummary"))
  def testSummaryValue(pathTokens: Seq[String], json: JValue, testFunc: SummaryTestFunc) =
    withClue(s"Summary test on path '${pathTokens.mkString(" -> ")}'") { testFunc(json) }

  @Test(groups = Array("parseSummary"))
  def testSummaryFileExist(): Unit = assert(summaryFile.exists, "Summary file does not exist")

  // Testing meta field of summary

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryPipelineName(): Unit =
    (summary \ "meta" \ "pipeline_name") shouldBe JString(pipelineName.toLowerCase)

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryPipelineVersion(): Unit =
    (summary \ "meta" \ "pipeline_version") shouldBe a[JString]

  //TODO: Add regex testing
  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCommitHash(): Unit =
    (summary \ "meta" \ "last_commit_hash") shouldBe a[JString]

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryOutputDir(): Unit =
    (summary \ "meta" \ "output_dir").extract[String] shouldBe outputDir.getAbsolutePath

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryCreation(): Unit =
    (summary \ "meta" \ "summary_creation") shouldBe a[JInt]

  @Test(dependsOnGroups = Array("parseSummary"))
  def summaryRunName(): Unit =
    (summary \ "meta" \ "run_name") shouldBe a[JString]

  def validateSummaryFile(summaryFile: JValue,
                          file: Option[File] = None,
                          md5: Option[String] = None): Unit = {
    summaryFile shouldBe a[JObject]
    (summaryFile \ "path") shouldBe a[JString]
    (summaryFile \ "md5") shouldBe a[JString]
    file.foreach(x => (summaryFile \ "path") shouldBe JString(x.getAbsolutePath))
    md5.foreach(x => (summaryFile \ "md5") shouldBe JString(x))
  }

  def summarySample(sampleId: String) = summary \ "samples" \ sampleId
  def summaryLibrary(sampleId: String, libId: String) = summary \ "samples" \ sampleId \ "libraries" \ libId
  def summaryRoot = summary

  type Executable = SummaryPipeline.Executable
  private var executables: Set[Executable] = Set()

  /** With this method an executable can be added that must exists in the summary */
  def addExecutable(exe: Executable): Unit = executables += exe

  @DataProvider(name = "executables")
  def executablesProvider = executables.map(Array(_)).toArray

  @Test(dataProvider = "executables", dependsOnGroups = Array("parseSummary"))
  def testExecutables(exe: Executable): Unit = withClue(s"Executable: $exe") {
    val summaryExe = summaryRoot \ pipelineName.toLowerCase \ "executables" \ exe.name
    summaryExe shouldBe a[JObject]
    exe.version match {
      case Some(r) =>
        (summaryExe \ "version") shouldBe a[JString]
        (summaryExe \ "version").extract[String] should fullyMatch regex r
      case _ => summaryExe \ "version" shouldBe JNothing
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

  @Test(dataProvider = "notExecutables", dependsOnGroups = Array("parseSummary"))
  def testNotExecutables(exe: String): Unit = withClue(s"Executable: $exe") {
    summaryRoot \ pipelineName.toLowerCase \ "executables" \ exe shouldBe JNothing
  }
}

object SummaryPipeline {

  case class Executable(name: String, version: Option[Regex] = None)

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
    val lowerBound = (expectedValue * (1 - errorMargin))
    val higherBound = (expectedValue * (1 + errorMargin))
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JInt(i)     => lowerBound < i.doubleValue() && i.doubleValue() < higherBound
        case JDouble(d)  => lowerBound < d && d < higherBound
        case JDecimal(d) => lowerBound.<(scala.math.BigDecimal(expectedValue)) && scala.math.BigDecimal(expectedValue).<(higherBound)
        case otherwise   => false
      }
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
