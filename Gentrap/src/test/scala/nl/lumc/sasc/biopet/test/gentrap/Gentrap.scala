package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.aligners._
import nl.lumc.sasc.biopet.test.annotations._
import nl.lumc.sasc.biopet.test.references.Reference
import nl.lumc.sasc.biopet.test.Pipeline._
import nl.lumc.sasc.biopet.test.MultisampleMapping

trait Gentrap
    extends MultisampleMapping
    with GtfAnnotation
    with RefflatAnnotation
    with RibosomalRefflatAnnotation
    with Reference
    with Aligner
    with ExpressionMeasures {

  override def defaultWgsMetrics = false

  override def defaultRnaMetrics = true

  def pipelineName = "gentrap"

  def summaryFile = new File(outputDir, "gentrap.summary.json")

  def removeRibosomalReads(): Option[Boolean] = None

  def callVariants: Option[Boolean] = None

  def strandProtocol: Option[String]

  override def configs = super.configs ++ expressionMeasuresConfig.toList

  override def args =
    super.args ++ cmdConfig("species", referenceSpecies) ++
      cmdConfig("reference_fasta", referenceFasta) ++
      cmdConfig("reference_name", referenceName) ++
      cmdConfig("strand_protocol", strandProtocol) ++
      cmdConfig("remove_ribosomal_reads", removeRibosomalReads) ++
      cmdConfig("call_variants", callVariants)
}
