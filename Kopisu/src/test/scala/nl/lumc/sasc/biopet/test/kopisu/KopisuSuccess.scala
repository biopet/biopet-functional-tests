package nl.lumc.sasc.biopet.test.kopisu

import java.io.File

import nl.lumc.sasc.biopet.test.{Biopet, PipelineSuccess}

/**
  * Created by Sander Bollen on 29-12-16.
  */
trait KopisuSuccess extends Kopisu with PipelineSuccess {

  override def inputBams = (1 to 20).
    map(_.toString).
    map(x => "kopisu" + File.separator + s"$x.sorted.bam").
    map(Biopet.fixtureFile(_)).toList

  override def ref = Biopet.fixtureFile("reference" + File.separator + "reference.fasta")


}
