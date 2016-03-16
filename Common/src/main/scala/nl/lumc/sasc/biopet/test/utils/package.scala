package nl.lumc.sasc.biopet.test

import java.io.{ File, FileInputStream, PrintWriter }
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

  def toDouble(s: String): Option[Double] = {
    try {
      Some(s.toDouble)
    } catch {
      case e: Exception => None
    }
  }

  def toInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case e: Exception => None
    }
  }

  /**
   *
   * Taken from http://alvinalexander.com/scala/scala-pearson-correlation-score-algorithm-programming-collective-intelligence
   * @param a List containing Doubles
   * @param b List containing Doubles
   * @return
   */
  def pearsonScore(a: Iterator[Double], b: Iterator[Double]): Option[Double] = {

    var n = 0
    var aSum = 0.0
    var bSum = 0.0
    var aSumSq = 0.0
    var bSumSq = 0.0
    var pSum = 0.0

    for ((aValue, bValue) <- a.zip(b)) {
      n += 1
      aSum += aValue
      bSum += bValue
      aSumSq += (aValue * aValue)
      bSumSq += (bValue * bValue)
      pSum += (aValue * bValue)
    }

    assert(!a.hasNext && !b.hasNext, "Sizes of both Lists are not equal")

    //  // calculate the pearson score
    val numerator = pSum - (aSum * bSum / n)
    val denominator = Math.sqrt((aSumSq - (aSum * aSum) / n) * (bSumSq - (bSum * bSum) / n))
    if (denominator == 0) None else Some(numerator / denominator)
  }

}
