package nl.lumc.sasc.biopet.test

import java.io.File

/**
 * Created by pjvan_thof on 5/26/15.
 */
object Biopet {
  def getBiopetJar: File = {
    System.getProperties.getProperty("biopet.jar") match {
      case s: String => {
        val file = new File(s)
        if (!file.exists()) throw new IllegalArgumentException("Biopet jar '" + file + "' does not exist")
        file
      }
      case _ => throw new IllegalArgumentException("No biopet jar found, please set the 'biopet.jar' property")
    }
  }

  def getOutputDir: File = {
    System.getProperties.getProperty("biopet.output_dir") match {
      case s: String => new File(s)
      case _         => throw new IllegalArgumentException("No output_dir found, please set the 'biopet.output_dir' property")
    }
  }

  def queueArgs: Seq[String] = {
    System.getProperties.getProperty("biopet.queueArgs") match {
      case s: String => s.split(" ").toSeq
      case _         => Nil
    }
  }
}
