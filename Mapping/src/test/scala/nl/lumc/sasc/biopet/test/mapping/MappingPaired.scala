package nl.lumc.sasc.biopet.test.mapping

import java.io.File

import nl.lumc.sasc.biopet.test.{ TestReference, Biopet }
import org.json4s._
import org.testng.annotations.Test

/**
 * Created by pjvan_thof on 10/7/15.
 */
trait MappingPaired extends MappingSuccess with TestReference {
  override def r1 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R1.fq.gz"))
  override def r2 = Some(Biopet.fixtureFile("samples" + File.separator + "wgs1" + File.separator + "R2.fq.gz"))
}

class MappingPairedTest extends MappingPaired

class MappingSkipTest extends MappingPaired {
  override def skipFlexiprep = Some(true)
  override def skipMetrics = Some(true)
  override def skipMarkDuplicates = Some(true)
}

class MappingPairedAutoChunkTest extends MappingPaired {
  override def chunking = Some(true)
  override def chunksize = Some(110000)
}

class MappingPairedChunkTest extends MappingPaired {
  override def chunking = Some(true)
  override def numberChunks = Some(4)
}
