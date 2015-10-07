package nl.lumc.sasc.biopet.test

import java.io.{PrintWriter, FileInputStream, File}
import java.util.zip.GZIPInputStream

import org.apache.commons.codec.digest.DigestUtils
import org.json4s.jackson.Serialization

/**
 * Created by pjvan_thof on 10/2/15.
 */
package object utils {
  implicit val formats = org.json4s.DefaultFormats

  /**
   * This method will create a temp config file to give as argument to Biopet
   * @param map Content of config file
   * @param name name prefix of temp file, defaults to "config"
   * @return
   */
  def createTempConfig(map: Map[String, Any], name: String = "config"): File = {
    val file = File.createTempFile(s"$name.", ".json")
    val writer = new PrintWriter(file)
    writer.println(Serialization.write(map))
    writer.close()
    file.deleteOnExit()
    file
  }

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
