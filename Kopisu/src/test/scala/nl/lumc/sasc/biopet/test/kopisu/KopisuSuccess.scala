package nl.lumc.sasc.biopet.test.kopisu

import java.io.File

import nl.lumc.sasc.biopet.test.{Biopet, PipelineSuccess}

/**
  * Created by Sander Bollen on 29-12-16.
  */
trait KopisuSuccess extends Kopisu with PipelineSuccess {

  override def inputBams =
    (1 to 20)
      .map(x => "kopisu" + File.separator + s"$x.sorted.bam")
      .map(Biopet.fixtureFile(_))
      .toList

  def ref = Biopet.fixtureFile("reference" + File.separator + "reference.fasta")

}

trait XhmmSuccess extends KopisuSuccess {
  (1 to 20).foreach { sample =>
    val bedPath = "xhmm" + File.separator + "beds" + File.separator + s"sample${sample}.bed"
    addMustHaveFile(bedPath)
  }

  addMustHaveFile("xhmm" + File.separator + "xhmm.xcnv")
  addMustHaveFile("xhmm" + File.separator + "xhmm.vcf")
  addMustHaveFile("xhmm" + File.separator + "xhmm.depths.data")
}
