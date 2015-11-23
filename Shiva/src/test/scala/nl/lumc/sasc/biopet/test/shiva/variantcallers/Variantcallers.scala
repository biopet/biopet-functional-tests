package nl.lumc.sasc.biopet.test.shiva.variantcallers

import java.io.File

import htsjdk.variant.vcf.VCFFileReader
import nl.lumc.sasc.biopet.test.SummaryPipeline.Executable
import nl.lumc.sasc.biopet.test.{ Pipeline, SummaryPipeline }
import nl.lumc.sasc.biopet.test.utils._

import scala.collection.JavaConversions._

/**
 * Created by pjvanthof on 16/11/15.
 */
trait Variantcallers extends Pipeline {
  def variantcallers: List[String] = Nil

  val variantcallersConfig = if (variantcallers.nonEmpty) Some(createTempConfig(Map("variantcallers" -> variantcallers))) else None

  override def configs = super.configs ::: variantcallersConfig.map(_ :: Nil).getOrElse(Nil)

  this match {
    case s: SummaryPipeline =>
      if (variantcallers.exists(!_.contains("haplotypecaller"))) s.addNotHavingExecutable("haplotypecaller")
      if (variantcallers.exists(!_.contains("unifiedgenotyper"))) s.addNotHavingExecutable("unifiedgenotyper")
      if (!variantcallers.contains("freebayes")) s.addNotHavingExecutable("freebayes")
      if (variantcallers.exists(!_.contains("bcftools"))) s.addNotHavingExecutable("bcftools")
      if (!variantcallers.contains("raw")) s.addNotHavingExecutable("mpileuptovcf")
    case _ =>
  }

  def testVariantcallerInfoTag(file: File): Unit = {
    val reader = new VCFFileReader(file, false)
    val lines = reader.getFileHeader.getOtherHeaderLines
    variantcallers foreach { caller =>
      assert(lines.exists(_.getValue.contains(s"RodBinding name=$caller")), s"Final vcf file is missing '$caller' in header for CombineVariants")
    }
    reader.close()
  }
}

trait Haplotypecaller extends Variantcallers {
  override def variantcallers = "haplotypecaller" :: super.variantcallers

  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("haplotypecaller", Some(""".+""".r)))
    case _                  =>
  }
}

trait HaplotypecallerGvcf extends Variantcallers {
  override def variantcallers = "haplotypecaller_gvcf" :: super.variantcallers

  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("haplotypecaller", Some(""".+""".r)))
    case _                  =>
  }
}

trait HaplotypecallerAllele extends Variantcallers {
  override def variantcallers = "haplotypecaller_allele" :: super.variantcallers

  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("haplotypecaller", Some(""".+""".r)))
    case _                  =>
  }
}

trait Unifiedgenotyper extends Variantcallers {
  override def variantcallers = "unifiedgenotyper" :: super.variantcallers

  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("unifiedgenotyper", Some(""".+""".r)))
    case _                  =>
  }
}

trait UnifiedgenotyperAllele extends Variantcallers {
  override def variantcallers = "unifiedgenotyper_allele" :: super.variantcallers

  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("unifiedgenotyper", Some(""".+""".r)))
    case _                  =>
  }
}

trait Freebayes extends Variantcallers {
  override def variantcallers = "freebayes" :: super.variantcallers

  this match {
    case s: SummaryPipeline => s.addExecutable(Executable("freebayes", Some(""".+""".r)))
    case _                  =>
  }
}

trait Bcftools extends Variantcallers {
  override def variantcallers = "bcftools" :: super.variantcallers

  this match {
    case s: SummaryPipeline =>
      s.addExecutable(Executable("bcftoolscall", Some(""".+""".r)))
      s.addExecutable(Executable("samtoolsmpileup", Some(""".+""".r)))
    case _ =>
  }
}

trait BcftoolsSinglesample extends Variantcallers {
  override def variantcallers = "bcftools_singlesample" :: super.variantcallers

  this match {
    case s: SummaryPipeline =>
      s.addExecutable(Executable("bcftoolscall", Some(""".+""".r)))
      s.addExecutable(Executable("samtoolsmpileup", Some(""".+""".r)))
    case _ =>
  }
}

trait Raw extends Variantcallers {
  override def variantcallers = "raw" :: super.variantcallers

  this match {
    case s: SummaryPipeline =>
      s.addExecutable(Executable("mpileuptovcf", Some(""".+""".r)))
      s.addExecutable(Executable("samtoolsmpileup", Some(""".+""".r)))
    case _ =>
  }
}
