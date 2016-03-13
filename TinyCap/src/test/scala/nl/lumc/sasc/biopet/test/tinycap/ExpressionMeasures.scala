package nl.lumc.sasc.biopet.test.tinycap

import nl.lumc.sasc.biopet.test.SummaryPipeline.Executable
import nl.lumc.sasc.biopet.test.{ Pipeline, SummaryPipeline }
import nl.lumc.sasc.biopet.test.utils._

/**
 * Copyright (c) 2015 Leiden University Medical Center - Sequencing Analysis Support Core <sasc@lumc.nl>
 *
 * @author Wibowo Arindrarto <w.arindrarto@lumc.nl>
 */
trait ExpressionMeasures extends Pipeline {

  def expressionMeasures: List[String] = Nil

  val expressionMeasuresConfig =
    if (expressionMeasures.nonEmpty)
      Some(createTempConfig(Map("expression_measures" -> expressionMeasures)))
    else None

  override def configs = super.configs ++ expressionMeasuresConfig.toList
}

trait FragmentsPerGene extends ExpressionMeasures {
  override def expressionMeasures = "fragments_per_gene" :: super.expressionMeasures
  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("htseqcount", Some(""".+""".r)))
    case _                  =>
  }
}

trait FragmentsPerSmallRna extends ExpressionMeasures {
  override def expressionMeasures = "fragments_per_smallrna" :: super.expressionMeasures
  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("htseqcount", Some(""".+""".r)))
    case _                  =>
  }
}


trait BaseCounts extends ExpressionMeasures {
  override def expressionMeasures = "base_counts" :: super.expressionMeasures
}

trait AllExpressionMeasures extends FragmentsPerGene
  with FragmentsPerSmallRna
  with BaseCounts
