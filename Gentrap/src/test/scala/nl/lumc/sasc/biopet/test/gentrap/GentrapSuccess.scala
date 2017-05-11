package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMappingSuccess
import org.testng.annotations.Test

/**
  * Created by pjvanthof on 05/11/15.
  */
trait GentrapSuccess extends Gentrap with MultisampleMappingSuccess {

  @Test
  def testVariantcalling: Unit = {
    val variantcallingDir = new File(outputDir, "variantcalling")
    if (callVariants == Some(true)) {
      assert(variantcallingDir.exists())
      assert(variantcallingDir.isDirectory)
    } else assert(!variantcallingDir.exists())
  }
}
