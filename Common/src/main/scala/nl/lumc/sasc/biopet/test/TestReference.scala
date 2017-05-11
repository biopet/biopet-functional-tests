package nl.lumc.sasc.biopet.test

import nl.lumc.sasc.biopet.test.Pipeline._

/**
  * Created by pjvan_thof on 9/17/15.
  */
trait TestReference extends Pipeline {
  abstract override def args =
    super.args ++
      cmdConfig("reference_fasta", Biopet.fixtureFile("reference/reference.fasta")) ++
      cmdConfig("bwamem:reference_fasta", Biopet.fixtureFile("reference/bwa/reference.fasta"))
}
