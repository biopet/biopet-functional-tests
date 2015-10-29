package nl.lumc.sasc.biopet.test.toucan

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by ahbbollen on 22-10-15.
 */
trait Toucan extends Pipeline {

  def pipelineName = "toucan"
  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def summaryFile = new File(outputDir, s"Toucan.summary.json")

  def inputVcf: Option[File] = None

  def goNLFile: Option[File] = None

  def exacFile: Option[File] = None

  def keepIntermediates: Boolean = false

  def normalizerMode: String = "standard"

  def args = cmdArg("-I", inputVcf) ++
    cmdConfig("species", referenceSpecies) ++
    cmdConfig("reference_name", referenceName) ++
    cmdConfig("gonl_vcf", goNLFile) ++
    cmdConfig("exac_vcf", exacFile) ++
    cmdConfig("mode", normalizerMode) ++
    cmdCondition("--keep_intermediates", keepIntermediates)

}
