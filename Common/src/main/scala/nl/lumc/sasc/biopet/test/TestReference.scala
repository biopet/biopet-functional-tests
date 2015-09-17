package nl.lumc.sasc.biopet.test

/**
 * Created by pjvan_thof on 9/17/15.
 */
trait TestReference extends Pipeline {
  override def args = super.args ++ Seq(
    "-cv", s"reference_fasta=${Biopet.fixtureFile("reference/reference.fasta")}",
    "-cv", s"bwa:reference_fasta=${Biopet.fixtureFile("reference/bwa/reference.fasta")}"
  )
}
