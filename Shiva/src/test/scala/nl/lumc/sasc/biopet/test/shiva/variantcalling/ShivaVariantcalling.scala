package nl.lumc.sasc.biopet.test.shiva.variantcalling

import java.io.File

import nl.lumc.sasc.biopet.test.Pipeline

/**
 * Created by pjvan_thof on 5/26/15.
 */
trait ShivaVariantcalling extends Pipeline {

  def pipelineName = "shivavariantcalling"

  def referenceSpecies: Option[String] = None

  def referenceName: Option[String] = None

  def aligner: Option[String] = None

  def bamFiles: List[File]

  def summaryFile = new File(outputDir, s"ShivaVariantcalling.summary.json")

  def args = configs.map(x => Seq("-config", x.getAbsolutePath)).toSeq.flatten ++
    referenceSpecies.collect { case species => Seq("-cv", s"species=$species") }.getOrElse(Seq()) ++
    referenceName.collect { case name => Seq("-cv", s"reference_name=$name") }.getOrElse(Seq()) ++
    bamFiles.flatMap(x => Seq("-BAM", x.getAbsolutePath))

  def configs: List[File]
}

