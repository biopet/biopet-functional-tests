package nl.lumc.sasc.biopet.test

import java.io.{ FileInputStream, File }
import java.util.zip.GZIPInputStream

import org.apache.commons.codec.digest.DigestUtils

/**
 * Created by pjvan_thof on 10/2/15.
 */
package object utils {
  /** Calculates the MD5 checksum of the given file. */
  def calcMd5(file: File): String = {
    val fis = new FileInputStream(file)
    val md5 = DigestUtils.md5Hex(fis)
    fis.close()
    md5
  }

  /** Calculates the MD5 checksum of the unzipped contents of the given gzipped-file. */
  def calcMd5Unzipped(file: File): String = {
    val fis = new GZIPInputStream(new FileInputStream(file))
    val md5 = DigestUtils.md5Hex(fis)
    fis.close()
    md5
  }
}
