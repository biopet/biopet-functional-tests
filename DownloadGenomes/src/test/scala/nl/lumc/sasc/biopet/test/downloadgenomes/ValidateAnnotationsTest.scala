package nl.lumc.sasc.biopet.test.downloadgenomes

import nl.lumc.sasc.biopet.test.PipelineSuccess
import org.testng.annotations.Test

/**
  * Created by pjvan_thof on 6-6-17.
  */
class ValidateAnnotationsTest extends ValidateAnnotations with PipelineSuccess {

  @Test(dataProvider = "genomesProvider")
  def testAnnotation(species: String, genomeName: String): Unit =
    withClue(s"$species-$genomeName") {
      testLogMustNotHave(s"Corrupt annotations files found for $species-$genomeName".r)
    }

  @Test(dataProvider = "genomesProvider")
  def testDbsnp(species: String, genomeName: String): Unit = withClue(s"$species-$genomeName") {
    testLogMustNotHave(s"Corrupt vcf file found for $species-$genomeName".r)
  }

  @Test(dataProvider = "genomesProvider")
  def testMissingAnnotation(species: String, genomeName: String): Unit =
    withClue(s"$species-$genomeName") {
      testLogMustNotHave(s"No features annotations found for $species-$genomeName".r)
    }

  @Test(dataProvider = "genomesProvider")
  def testMissingDbsnp(species: String, genomeName: String): Unit =
    withClue(s"$species-$genomeName") {
      testLogMustNotHave(s"Genome '$species-$genomeName is missing dbsnp files".r)
    }

}
