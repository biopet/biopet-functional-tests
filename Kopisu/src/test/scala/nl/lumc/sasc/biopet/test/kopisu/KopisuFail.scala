package nl.lumc.sasc.biopet.test.kopisu

import java.io.File

import nl.lumc.sasc.biopet.test.{Biopet, PipelineFail}

/**
  * Created by Sander Bollen on 29-12-16.
  */
trait KopisuFail extends Kopisu with PipelineFail {

  def ref = Biopet.fixtureFile("reference" + File.separator + "reference.fasta")

}
