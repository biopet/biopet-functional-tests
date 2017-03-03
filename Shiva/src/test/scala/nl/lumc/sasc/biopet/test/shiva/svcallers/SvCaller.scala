package nl.lumc.sasc.biopet.test.shiva.svcallers

trait SvCaller {

  def svCallerName: String
  /** Types of structural variants the method is designed to detect. */
  def supportedTypes: List[String]

}

class Breakdancer extends SvCaller {

  def svCallerName = "breakdancer"
  def supportedTypes = List("INS", "DEL", "INV", "CTX", "ITX")

}

class Clever extends SvCaller {

  def svCallerName = "clever"
  def supportedTypes = List("INS", "DEL") // clever detects only insertions and deletions

}

class Delly extends SvCaller {

  def svCallerName = "delly"
  def supportedTypes = List("DEL", "INV", "TRA") // delly doesn't report insertions nor intrachromosomal translocations

}