package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.shiva.variantcallers.Variantcallers

/**
  * Created by pjvan_thof on 5/26/15.
  */
trait ShivaVariantcalling extends Pipeline with Variantcallers {

  def pipelineName = "shivavariantcalling"

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def namePrefix: Option[String] = Some("multisample")

  def bamFiles: List[File]

  def summaryFile = new File(outputDir, s"ShivaVariantcalling.summary.json")

  def runContest: Option[Boolean] = None

  def popFile: Option[File] = None

  override def args: Seq[String] =
    bamFiles.flatMap(x => Seq("-BAM", x.getAbsolutePath)) ++
      cmdConfig("species", referenceSpecies) ++
      cmdConfig("reference_name", referenceName) ++
      cmdConfig("name_prefix", namePrefix) ++
      cmdConfig("run_contest", runContest) ++
      cmdConfig("popfile", popFile)
}
