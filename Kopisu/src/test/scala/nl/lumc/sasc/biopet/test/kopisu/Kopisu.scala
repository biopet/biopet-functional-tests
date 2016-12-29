package nl.lumc.sasc.biopet.test.kopisu

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.references.Reference

/**
  * Created by Sander Bollen on 29-12-16.
  */
trait Kopisu extends Pipeline with Reference {

  def pipelineName = "kopisu"

  def inputBams: List[File] = Nil

  override def args = super.args ++ inputBams.foldLeft[Seq[String]](Seq()) { (acc, v) =>
    acc ++ cmdArg("-BAM", v)
  }

}
