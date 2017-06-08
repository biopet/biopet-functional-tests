package nl.lumc.sasc.biopet.test

import java.io.File

import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import nl.lumc.sasc.biopet.utils.ConfigUtils
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb.Implicts._
import nl.lumc.sasc.biopet.utils.summary.db.SummaryDb._
import org.json4s.jackson.JsonMethods.{asJsonNode, parse}
import org.testng.annotations.{DataProvider, Test}

import scala.collection.JavaConverters._
import scala.collection.mutable.{ListBuffer, Map => MutMap}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import scala.util.matching.Regex

/**
  * Created by pjvanthof on 19/09/15.
  */
trait SummaryPipeline extends PipelineSuccess {

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
    statsTests(summaryGroup) += path -> (SummaryTest(test, shouldExist) :: statsTests(summaryGroup)
      .getOrElse(path, Nil))
  }

  @DataProvider(name = "statsTests")
  def summaryStatsProvider() = {
    (for ((group, functions) <- statsTests) yield Array(group, functions)).toArray
  }

  @Test(dataProvider = "statsTests", dependsOnGroups = Array("summary"))
  def testSummaryStats(summaryGroup: SummaryGroup,
                       functions: MutMap[List[String], List[SummaryTest]]): Unit = {
    val statsPaths = functions.keys.map(l => l.mkString("->") -> l).toMap
    val results = summaryDb.getStatKeys(
      runId,
      summaryGroup.pipeline,
      summaryGroup.module.map(ModuleName).getOrElse(NoModule),
      summaryGroup.sample.map(SampleName).getOrElse(NoSample),
      summaryGroup.library.map(LibraryName).getOrElse(NoLibrary),
      statsPaths
    )
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
      throw new Exception(
        s"Error found in summary group: $summaryGroup\n${messages.mkString("\n")}")
    }
  }

  private var settingsTests: MutMap[SummaryGroup, MutMap[List[String], List[Any => Unit]]] =
    MutMap()

  def addSettingsTest(summaryGroup: SummaryGroup,
                      path: List[String],
                      function: Any => Unit): Unit = {
    if (!settingsTests.contains(summaryGroup)) settingsTests += (summaryGroup) -> MutMap()
    settingsTests(summaryGroup) += path -> (function :: settingsTests(summaryGroup).getOrElse(path,
                                                                                              Nil))
  }

  @DataProvider(name = "settingsTests")
  def summarySettingsProvider() = {
    (for ((group, functions) <- settingsTests) yield Array(group, functions)).toArray
  }

  @Test(dataProvider = "settingsTests", dependsOnGroups = Array("summary"))
  def testSummarySettings(summaryGroup: SummaryGroup,
                          functions: MutMap[List[String], List[Any => Unit]]): Unit = {
    val settingsPaths = functions.keys.map(l => l.mkString("->") -> l).toMap
    val results = summaryDb.getSettingKeys(
      runId,
      summaryGroup.pipeline,
      summaryGroup.module.map(ModuleName).getOrElse(NoModule),
      summaryGroup.sample.map(SampleName).getOrElse(NoSample),
      summaryGroup.library.map(LibraryName).getOrElse(NoLibrary),
      settingsPaths
    )
    val errors = new ListBuffer[Throwable]
    functions.foreach { x =>
      x._2.foreach { f =>
        try {
          withClue(s"group: $summaryGroup, path: ${x._1}") {
            val value = results(x._1.mkString("->"))
            require(value.isDefined, s"Value does not exist, path: ${x._1}")
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
      throw new Exception(
        s"Error found in summary group: $summaryGroup\n${messages.mkString("\n")}")
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
    val file = Await.result(
      summaryDb.getFile(
        runId,
        fileTest.group.pipeline,
        fileTest.group.module.map(ModuleName).getOrElse(NoModule),
        fileTest.group.sample.map(SampleName).getOrElse(NoSample),
        fileTest.group.library.map(LibraryName).getOrElse(NoLibrary),
        fileTest.key
      ),
      Duration.Inf
    )
    if (fileTest.summaryShouldContain) {
      file should not be empty
      val f =
        if (file.get.path.startsWith("./"))
          new File(outputDir, file.get.path.stripPrefix("./")).getAbsoluteFile
        else new File(file.get.path)
      fileTest.path.foreach(f shouldBe _)
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
    val exesDb =
      Await.result(summaryDb.getExecutables(runId = Some(runId), toolName = Some(exe.name)),
                   Duration.Inf)
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
    val exesDb = Await.result(summaryDb.getExecutables(runId = Some(runId), toolName = Some(exe)),
                              Duration.Inf)
    exesDb shouldBe empty
  }

  @DataProvider(name = "moduleSchemas")
  def moduleSchemasProvider() = {
    val modulesUsed = Await.result(summaryDb.getModules(runId = Some(this.runId)), Duration.Inf)
    var moduleSchemas: Array[Array[Object]] = Array()
    for (module <- modulesUsed) {
      SummaryPipeline.moduleSchemas.find(schema => schema._1.findFirstIn(module.name).nonEmpty) match {
        case Some(schema) => moduleSchemas :+= Array(module.name, schema._2)
        case _ =>
      }
    }
    moduleSchemas
  }

  // the test is needed as errors in data provider methods are ignored and don't get reported as failures in test results,
  // the next test, 'testModuleStats()', would otherwise be simply skipped when it's data provider fails
  @Test(dependsOnGroups = Array("summary"))
  def testModuleSchemas(): Unit = {
    try {
      moduleSchemasProvider()
    } catch {
      case e: Throwable =>
        fail("Error loading Json schemas for validating summary's Stats table", e)
    }
  }

  @Test(dataProvider = "moduleSchemas", dependsOnGroups = Array("summary"))
  def testModuleStats(moduleName: String, schema: JsonSchema): Unit = {
    val stats = Await.result(
      summaryDb.getStats(runId = Some(this.runId), module = Some(ModuleName(moduleName))),
      Duration.Inf)
    for (record <- stats) {
      assert(
        schema.validate(asJsonNode(parse(record.content)), true).iterator().asScala.toSeq.isEmpty,
        s"Json schema check failed for statistics from module '$moduleName' (sampleId-${record.sampleId}, libraryId-${record.library})"
      )
    }
  }

}

case class SummaryGroup(pipeline: String,
                        module: Option[String] = None,
                        sample: Option[String] = None,
                        library: Option[String] = None)

case class Executable(name: String, version: Option[Regex] = None)

object SummaryPipeline {

  val moduleSchemas: Map[Regex, JsonSchema] = {

    val schemaFactory: JsonSchemaFactory = JsonSchemaFactory.byDefault()
    val moduleSchemas =
      ClassLoader.getSystemResource("nl/lumc/sasc/biopet/test/module_schemas.yml").toURI()

    ConfigUtils
      .fileToConfigMap(new File(moduleSchemas))
      .map({
        case (moduleName, schemaFile) => {
          val schemaURI = ClassLoader.getSystemResource(schemaFile.toString).toURI().toString
          s"^$moduleName$$".r -> schemaFactory.getJsonSchema(schemaURI)
        }
      })
  }

}
