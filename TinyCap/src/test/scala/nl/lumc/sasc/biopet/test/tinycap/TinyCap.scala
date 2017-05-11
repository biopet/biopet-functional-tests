package nl.lumc.sasc.biopet.test.tinycap

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMapping
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.aligners.Aligner
import nl.lumc.sasc.biopet.test.annotations.{GffAnnotation, RefflatAnnotation, GtfAnnotation}
import nl.lumc.sasc.biopet.test.references.Reference

trait TinyCap
    extends MultisampleMapping
    with GtfAnnotation
    with GffAnnotation
    with RefflatAnnotation
    with Reference
    with Aligner
    with ExpressionMeasures {

  override def defaultWgsMetrics = false

  override def defaultRnaMetrics = false

  def pipelineName = "tinycap"

  def summaryFile = new File(outputDir, "tinycap.summary.json")

  override def configs = super.configs ++ expressionMeasuresConfig.toList

  override def args =
    super.args ++ cmdConfig("species", referenceSpecies) ++
      cmdConfig("reference_fasta", referenceFasta) ++
      cmdConfig("reference_name", referenceName)
}
