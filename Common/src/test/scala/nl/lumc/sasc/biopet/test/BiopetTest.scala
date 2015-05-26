package nl.lumc.sasc.biopet.test

import java.io.File

import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 5/26/15.
 */
class BiopetTest extends TestNGSuite with Matchers {

  @Test def bla: Unit = {

    BiopetTest.getBiopetJar

  }

}

object BiopetTest {
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
}