package nl.lumc.sasc.biopet.test.toucan

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.references.Reference

/**
  * Created by ahbbollen on 22-10-15.
  */
trait Toucan extends Pipeline with Reference {

  def pipelineName = "toucan"

  def inputVcf: Option[File] = None

  def goNLFile: Option[File] = None

  def exacFile: Option[File] = None

  def normalizerMode: String = "standard"

  def enableScatter: Boolean = false

  def vepVersion: Option[String] = None

  def vepOffline: Option[Boolean] = Some(true)

  override def args: Seq[String] =
    super.args ++
      cmdArg("-Input", inputVcf) ++
      cmdConfig("enable_scatter", enableScatter) ++
      cmdConfig("gonl_vcf", goNLFile) ++
      cmdConfig("exac_vcf", exacFile) ++
      cmdConfig("mode", normalizerMode) ++
      cmdConfig("varianteffectpredictor:offline", vepOffline) ++
      cmdConfig("vep_version", vepVersion)
}
