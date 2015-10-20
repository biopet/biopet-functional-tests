package nl.lumc.sasc.biopet.test

import java.io.{ File, PrintWriter }

import org.json4s._
import org.scalatest._, matchers._
import org.scalatest.testng.TestNGSuite
import org.testng.SkipException
import org.testng.annotations.{ DataProvider, Test, BeforeClass }

import scala.io.Source
import scala.sys.process._
import scala.util.matching.Regex

/**
 * Created by pjvan_thof on 6/30/15.
 */

trait Pipeline extends TestNGSuite with Matchers with JValueMatchers {

  def outputDir = new File(Biopet.getOutputDir, this.getClass.getName.stripPrefix("nl.lumc.sasc.biopet.test."))

  def outputDirArg = Option(outputDir)

  def logFile = new File(outputDir, "run.log")

  def pipelineName: String

  def args: Seq[String]

  private var _exitValue = -1
  def exitValue = _exitValue

  def memoryArg = "-Xmx150m"

  def retries = Option(5)
  def allowRetries = 0

  /**
   * When override this to true the pipeline is marked as functional.
   * This is only executed when property "biopet.functionalTests" is set true
   */
  def functionalTest = false

  /** This is default true, when override to false the pipeline will only execute a dry run */
  def run = true

  @BeforeClass
  def runPipeline(): Unit = {
    if (functionalTest && !Biopet.functionalTests) throw new SkipException("Functional tests are disabled")
    // Running pipeline
    _exitValue = Pipeline.runPipeline(pipelineName, outputDir, outputDirArg, args, logFile, memoryArg, retries, run)
  }

  @DataProvider(name = "not_allowed_reties")
  def notAllowedRetries = {
    (for (i <- (allowRetries + 1) to retries.getOrElse(1)) yield {
      Array("d", i)
    }).toArray
  }

  @Test(dataProvider = "not_allowed_reties", dependsOnGroups = Array("parseLog"))
  def testRetry(dummy: String, retry: Int): Unit = {
    val s = s"Reset for retry attempt $retry of ${retries.getOrElse(0)}"
    require(!logLines.exists(_.contains(s)), s"${retry}e retry found but not allowed")
  }

  @Test(priority = -1) def exitcode() = exitValue shouldBe 0
  @Test def outputDirExist() = assert(outputDir.exists())

  private var _logLines: List[String] = _
  def logLines = _logLines

  @Test(groups = Array("parseLog"))
  def logFileExist() = {
    assert(logFile.exists())
    _logLines = Source.fromFile(logFile).getLines().toList
  }

  private var logMustHave: List[Regex] = Nil
  def logMustHave(r: Regex): Unit = logMustHave :+= r

  @DataProvider(name = "log_must_have")
  def logMustHaveProvider = logMustHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_have", dependsOnGroups = Array("parseLog"))
  def testLogMustHave(r: Regex): Unit = withClue(s"regex: $r") {
    assert(logLines.exists(r.findFirstMatchIn(_).isDefined), s"Logfile does not contains: $r")
  }

  private var logMustNotHave: List[Regex] = Nil
  def logMustNotHave(r: Regex): Unit = logMustNotHave :+= r

  @DataProvider(name = "log_must_not_have")
  def logMustNotHaveProvider = logMustNotHave.map(Array(_)).toArray

  @Test(dataProvider = "log_must_not_have", dependsOnGroups = Array("parseLog"))
  def testLogMustNotHave(r: Regex): Unit = withClue(s"regex: $r") {
    val i = logLines.indexWhere(r.findFirstMatchIn(_).isDefined)
    assert(i == -1, s"at line number ${i + 1} in logfile does contains: $r")
  }
}

object Pipeline {
  def runPipeline(pipelineName: String,
                  outputDir: File,
                  outputDirArg: Option[File],
                  args: Seq[String],
                  logFile: File,
                  memoryArg: String,
                  retries: Option[Int],
                  run: Boolean) = {
    val cmd = Seq("java", memoryArg, "-jar", Biopet.getBiopetJar.toString, "pipeline", pipelineName) ++
      Biopet.queueArgs ++
      cmdArg("-retry", retries) ++
      cmdCondition("-run", run) ++
      cmdConfig("output_dir", outputDirArg) ++
      args
    if (!outputDir.exists()) outputDir.mkdirs()

    if (logFile.exists()) logFile.delete()
    val writer = new PrintWriter(logFile)
    def writeLine(line: String): Unit = {
      writer.println(line)
      writer.flush()
    }
    val process = Process(cmd, outputDir).run(ProcessLogger(writeLine(_)))
    val exitValue = process.exitValue()
    writer.close()

    exitValue
  }

  def cmdArg(argName: String, value: Any): Seq[String] = {
    value match {
      case Some(v) => Seq(argName, v.toString)
      case None    => Seq()
      case _       => Seq(argName, value.toString)
    }
  }

  def cmdCondition(argName: String, condition: Boolean): Seq[String] = {
    if (condition) Seq(argName) else Seq()
  }

  def cmdConfig(configKey: String, value: Any): Seq[String] = {
    value match {
      case Some(v) => Seq("-cv", s"${configKey}=${v.toString}")
      case None    => Seq()
      case _       => Seq("-cv", s"${configKey}=${value.toString}")
    }
  }

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

  class JValueStringMatcher(expectedValue: String) extends Matcher[JValue] {
    def apply(left: JValue) = {
      def testFunc: Boolean = left match {
        case JString(s) => s == expectedValue
        case otherwise  => false
      }
      makeMatchResult(testFunc, left, expectedValue)
    }
  }

  def haveValue(expectedValue: Int) = new JValueIntMatcher(expectedValue)
  def haveValue(expectedValue: Double) = new JValueDoubleMatcher(expectedValue)
  def haveValue(expectedValue: String) = new JValueStringMatcher(expectedValue)
  def existAsFile = new JValueFileExistMatcher
}
