package nl.lumc.sasc.biopet.test.samples

import nl.lumc.sasc.biopet.test.Pipeline

/**
  * Created by pjvan_thof on 11/16/15.
  */
trait Samples extends Pipeline {

  /** This should return a Map[<sampleName>, List[<libName>]] */
  def samples: Map[String, Set[String]] = Map()

  private[samples] def addSampleLibrary(original: Map[String, Set[String]],
                                        sample: String,
                                        library: String*) = {
    original + (sample -> (original.getOrElse(sample, Set()) ++ library))
  }
}
