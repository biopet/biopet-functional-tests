package nl.lumc.sasc.biopet.test.basty

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMapping
import nl.lumc.sasc.biopet.test.Pipeline.cmdConfig
import nl.lumc.sasc.biopet.test.aligners.Aligner
import nl.lumc.sasc.biopet.test.references.Reference

/**
  * Created by pjvan_thof on 5/26/15.
  */
trait Basty extends MultisampleMapping with Reference with Aligner {

  def pipelineName = "basty"

  def useIndelRealigner: Option[Boolean] = None

  def useBaseRecalibration: Option[Boolean] = None

  def usePrintReads: Option[Boolean] = None

  def dbsnpVcfFile: Option[File] = None

  // This is set on true because bgfs does not work on test data
  def raxmlNoBfgs: Option[Boolean] = Some(true)

  // Default is 100 but to limit the number of jobs we set the default to 5 here
  def bootRuns: Option[Int] = Some(5)

  def executeGubbins: Option[Boolean] = Some(false)

  override def args: Seq[String] =
    super.args ++
      cmdConfig("use_indel_realigner", useIndelRealigner) ++
      cmdConfig("use_base_recalibration", useBaseRecalibration) ++
      cmdConfig("dbsnp_vcf", dbsnpVcfFile) ++
      cmdConfig("use_printreads", usePrintReads) ++
      cmdConfig("raxml:no_bfgs", raxmlNoBfgs) ++
      cmdConfig("boot_runs", bootRuns) ++
      cmdConfig("execute_gubbins", executeGubbins)

}
