package nl.lumc.biopet.sasc.test.flexiprep

import nl.lumc.sasc.biopet.test.Biopet
import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test

import scala.sys.process._

/**
 * Created by pjvan_thof on 5/26/15.
 */
class FlexiprepTest extends TestNGSuite with Matchers {
  @Test
  def basicTest: Unit = {
    val cmd = Seq("java", "-jar", Biopet.getBiopetJar.toString, "pipeline", "flexiprep", "-l", "debug")
    val process = Process(cmd).run(ProcessLogger(println(_)))
    println("exit: " + process.exitValue())
  }
}
