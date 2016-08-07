package nl.lumc.sasc.biopet.test

import java.io.File

/**
 * Created by pjvan_thof on 5/26/15.
 */
object Biopet {
  lazy val getBiopetJar: File = {
    System.getProperties.getProperty("biopet.jar") match {
      case s: String => {
        val file = new File(s)
        if (!file.exists()) throw new IllegalArgumentException("Biopet jar '" + file + "' does not exist")
        file
      }
      case _ => throw new IllegalArgumentException("No biopet jar found, please set the 'biopet.jar' property")
    }
  }

  lazy val getOutputDir: File = {
    System.getProperties.getProperty("biopet.output_dir") match {
      case s: String => new File(s)
      case _         => throw new IllegalArgumentException("No output_dir found, please set the 'biopet.output_dir' property")
    }
  }

  lazy val queueArgs: Seq[String] = {
    System.getProperties.getProperty("biopet.queueArgs") match {
      case s: String => s.split(" ").toSeq
      case _         => Nil
    }
  }

  lazy val functionalTests: Boolean = {
    System.getProperties.getProperty("biopet.functionalTests") match {
      case "false" => false
      case null    => false
      case _       => true
    }
  }

  lazy val integrationTests: Boolean = {
    System.getProperties.getProperty("biopet.integrationTests") match {
      case "false" => false
      case null    => true
      case _       => true
    }
  }

  lazy val fixtureDir: File = {
    System.getProperties.getProperty("biopet.fixture_dir") match {
      case s: String => {
        val dir = new File(s)
        require(dir.exists(), "Fixture directory does not exist: " + s)
        require(dir.isDirectory, "Fixture directory is not a directory: " + s)
        dir
      }
      case _ => throw new IllegalArgumentException("No output_dir found, please set the 'biopet.fixture_dir' property")
    }
  }

  def fixtureFile(paths: String*): File = {
    val file = new File(fixtureDir, paths.mkString(File.separator))
    require(file.exists(), "Fixture file does not exist: " + file)
    require(file.canRead(), "Fixture file is not readable: " + file)
    file.getAbsoluteFile
  }
}
