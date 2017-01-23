package nl.lumc.sasc.biopet.test.kopisu

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by Sander Bollen on 29-12-16.
 */
trait Kopisu extends Pipeline {

  def pipelineName = "kopisu"

  def inputBams: List[File] = Nil
  def ref: File

  override def args = super.args ++ inputBams.foldLeft[Seq[String]](Seq()) { (acc, v) =>
    acc ++ cmdArg("-BAM", v)
  } ++ cmdConfig("reference_fasta", ref)

}
