package nl.lumc.biopet.sasc.test.flexiprep

import java.io.File

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
  def basicDryTest: Unit = {

    println(new File(".").getAbsolutePath)

    val cmd = Seq("java", "-jar", Biopet.getBiopetJar.toString, "pipeline", "flexiprep", "-R1", "bla.fq.gz")
    val process = Process(cmd).run()
    val exitValue = process.exitValue()

    println("exit: " + exitValue)

    exitValue shouldBe 0
  }
}
