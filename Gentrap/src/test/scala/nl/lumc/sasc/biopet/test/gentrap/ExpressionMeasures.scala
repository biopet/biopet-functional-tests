package nl.lumc.sasc.biopet.test.gentrap

import java.io.File

import nl.lumc.sasc.biopet.test.Executable
import nl.lumc.sasc.biopet.test.{Pipeline, SummaryPipeline}
import nl.lumc.sasc.biopet.test.utils._

/**
  * Copyright (c) 2015 Leiden University Medical Center - Sequencing Analysis Support Core <sasc@lumc.nl>
  *
  * @author Wibowo Arindrarto <w.arindrarto@lumc.nl>
  */
trait ExpressionMeasures extends Pipeline {

  def expressionMeasures: List[String] = Nil

  val expressionMeasuresConfig: Option[File] =
    if (expressionMeasures.nonEmpty)
      Some(createTempConfig(Map("expression_measures" -> expressionMeasures)))
    else None

  override def configs: List[File] = super.configs ++ expressionMeasuresConfig.toList
}

trait FragmentsPerGene extends ExpressionMeasures {
  override def expressionMeasures: List[String] = "fragments_per_gene" :: super.expressionMeasures
  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("htseqcount", Some(""".+""".r)))
    case _ =>
  }
}

trait BaseCounts extends ExpressionMeasures {
  override def expressionMeasures: List[String] = "base_counts" :: super.expressionMeasures
}

trait Stringtie extends ExpressionMeasures {
  override def expressionMeasures: List[String] = "stringtie" :: super.expressionMeasures
  this match {
    case s: SummaryPipeline =>
      s.addExecutable(Executable("stringtie", Some(""".+""".r)))
      s.addExecutable(Executable("stringtiemerge", Some(""".+""".r)))
    case _ =>
  }
}
trait CufflinksStrict extends ExpressionMeasures {
  override def expressionMeasures: List[String] = "cufflinks_strict" :: super.expressionMeasures
  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("cufflinks", Some(""".+""".r)))
    case _ =>
  }
}

trait CufflinksGuided extends ExpressionMeasures {
  override def expressionMeasures: List[String] = "cufflinks_guided" :: super.expressionMeasures
  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("cufflinks", Some(""".+""".r)))
    case _ =>
  }
}

trait CufflinksBlind extends ExpressionMeasures {
  override def expressionMeasures: List[String] = "cufflinks_blind" :: super.expressionMeasures
  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("cufflinks", Some(""".+""".r)))
    case _ =>
  }
}

trait AllExpressionMeasures
    extends FragmentsPerGene
    with BaseCounts
    with CufflinksStrict
    with CufflinksGuided
    with CufflinksBlind
    with Stringtie
