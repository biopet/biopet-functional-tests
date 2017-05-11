package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.aligners.BwaMem
import nl.lumc.sasc.biopet.test.references.TestReference
import nl.lumc.sasc.biopet.test.{Biopet, PipelineFail}
import org.testng.annotations.Test

/**
  * Created by pjvan_thof on 10/7/15.
  */
class MappingNoR1ArgTest extends Mapping with PipelineFail with TestReference with BwaMem {
  logMustNotHave("""FunctionEdge - Starting""".r)

  logMustHave("""Argument with name '--inputR1' \(-R1\) is missing""".r)
}

class MappingDryRunNoR1ArgTest extends Mapping with PipelineFail with TestReference with BwaMem {
  override def run = false
  logMustNotHave("""FunctionEdge - Starting""".r)

  logMustHave("""Argument with name '--inputR1' \(-R1\) is missing""".r)
}

class MappingSingleR1NotExistTest
    extends Mapping
    with PipelineFail
    with TestReference
    with BwaMem {
  override def r1 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R1.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r1.get.exists(), s"this file should not exist: $r1")
}

class MappingPairedR1NotExistTest
    extends Mapping
    with PipelineFail
    with TestReference
    with BwaMem {
  override def r1 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R1.fq"))
  override def r2 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))

  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r1.get.exists(), s"this file should not exist: $r1")
}

class MappingPairedR2NotExistTest
    extends Mapping
    with PipelineFail
    with TestReference
    with BwaMem {
  override def r1 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
  override def r2 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R2.fq"))

  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r2.get.exists(), s"this file should not exist: $r2")
}

class MappingDryRunSingleR1NotExistTest
    extends Mapping
    with PipelineFail
    with TestReference
    with BwaMem {
  override def run = false
  override def r1 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R1.fq"))
  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r1.get.exists(), s"this file should not exist: $r1")
}

class MappingDryRunPairedR1NotExistTest
    extends Mapping
    with PipelineFail
    with TestReference
    with BwaMem {
  override def run = false
  override def r1 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R1.fq"))
  override def r2 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))

  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r1.get.exists(), s"this file should not exist: $r1")
}

class MappingDryRunPairedR2NotExistTest
    extends Mapping
    with PipelineFail
    with TestReference
    with BwaMem {
  override def run = false
  override def r1 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
  override def r2 = Some(new File(Biopet.fixtureDir, "This_should_not_exist.R2.fq"))

  logMustNotHave("""FunctionEdge - Starting""".r)

  @Test def fileNotExist() = assert(!r2.get.exists(), s"this file should not exist: $r2")
}

class MappingNoOutputDirTest extends Mapping with PipelineFail with TestReference with BwaMem {
  override def outputDirArg = None
  override def r1 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))

  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: output_dir""".r)
}

class MappingDryRunNoOutputDirTest
    extends Mapping
    with PipelineFail
    with TestReference
    with BwaMem {
  override def outputDirArg = None
  override def run = false
  override def r1 =
    Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))

  logMustNotHave("""FunctionEdge - Starting""".r)
  logMustHave("""Value does not exist but is required, key: output_dir""".r)
}
