package nl.lumc.sasc.biopet.test

import nl.lumc.sasc.biopet.test.Pipeline._

/**
 * Created by pjvan_thof on 2/5/16.
 */
trait MultisampleMapping extends Pipeline {

  def bamToFastq: Option[Boolean] = None

  def correctReadgroups: Option[Boolean] = None

  def executeBam2wig: Option[Boolean] = None

  def defaultExecuteBam2wig = true

  final def bam2wigShouldRun = executeBam2wig.getOrElse(defaultExecuteBam2wig)

  def wgsMetrics: Option[Boolean] = None

  def defaultWgsMetrics = true

  final def wgsMetricsShouldRun = wgsMetrics.getOrElse(defaultWgsMetrics)

  def rnaMetrics: Option[Boolean] = None

  def defaultRnaMetrics = false

  final def rnaMetricsShouldRun = rnaMetrics.getOrElse(defaultRnaMetrics)

  def skipFlexiprep: Option[Boolean] = None

  def defaultSkipFlexiprep = false

  def flexiprepShouldRun = !skipFlexiprep.getOrElse(defaultSkipFlexiprep)

  def mappingToGears: Option[String] = Some("none")

  def mergeStrategy: Option[String] = None

  override def args = super.args ++
    cmdConfig("bam_to_fastq", bamToFastq) ++
    cmdConfig("correct_readgroups", correctReadgroups) ++
    cmdConfig("execute_bam2wig", executeBam2wig) ++
    cmdConfig("wgs_metrics", wgsMetrics) ++
    cmdConfig("rna_metrics", rnaMetrics) ++
    cmdConfig("mapping_to_gears", mappingToGears) ++
    cmdConfig("merge_strategy", mergeStrategy)

}
