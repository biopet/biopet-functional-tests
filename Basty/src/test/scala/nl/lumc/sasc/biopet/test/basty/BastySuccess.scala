package nl.lumc.sasc.biopet.test.basty

import java.io.File

import nl.lumc.sasc.biopet.test.MultisampleMappingSuccess

/**
  * Created by pjvan_thof on 27-5-16.
  */
trait BastySuccess extends Basty with MultisampleMappingSuccess {

  override def multiSampleMappingPipelineName = "shiva"

  override def samplePreprocessBam(sampleId: String) =
    new File(
      super.samplePreprocessBam(sampleId).getAbsolutePath.stripSuffix(".bam") +
        (if (useIndelRealigner.getOrElse(true)) ".realign.bam" else ".bam"))

  override def libraryPreprecoessBam(sampleId: String, libId: String) =
    new File(
      super.libraryPreprecoessBam(sampleId, libId).getAbsolutePath.stripSuffix(".bam") +
        (if (useBaseRecalibration
               .getOrElse(true) && dbsnpVcfFile.isDefined && !usePrintReads.contains(false))
           ".baserecal.bam"
         else ".bam"))

}
