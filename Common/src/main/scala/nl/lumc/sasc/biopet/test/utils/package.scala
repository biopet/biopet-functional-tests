package nl.lumc.sasc.biopet.test

import java.io.{File, FileInputStream, PrintWriter}
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
  def pearsonScore(a: List[Double], b: List[Double]): Option[Double] = {

    assert(a.size == b.size, "Sizes of both Maps are not equal")
    val n = a.size
    // add up all the preferences
    val sum1 = a.sum
    val sum2 = b.sum

    // sum up the squares
    val sum1Sq = a.foldLeft(0.0)(_ + Math.pow(_, 2))
    val sum2Sq = b.foldLeft(0.0)(_ + Math.pow(_, 2))

    // sum up the products
    val pSum = (a.view.zipWithIndex foldLeft 0.0) {
      case (acc, (value, index)) => acc + (value * b(index))
    }

    //  // calculate the pearson score
    val numerator = pSum - (sum1 * sum2 / n)
    val denominator = Math.sqrt((sum1Sq - Math.pow(sum1, 2) / n) * (sum2Sq - Math.pow(sum2, 2) / n))
    if (denominator == 0) None else Some(numerator / denominator)
  }

}
