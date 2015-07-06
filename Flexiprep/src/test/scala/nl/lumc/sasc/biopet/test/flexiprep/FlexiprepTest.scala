package nl.lumc.sasc.biopet.test.flexiprep

import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{ Factory, Test }

/**
 * Created by pjvan_thof on 7/6/15.
 */
class FlexiprepTest extends TestNGSuite with Matchers {
  @Factory def blabla: Array[Object] = {
    val bla = new AbstractFlexiprepTest {

    }

    val bla2 = new AbstractFlexiprepTest {

    }

    Array(bla, bla2)
  }
}
