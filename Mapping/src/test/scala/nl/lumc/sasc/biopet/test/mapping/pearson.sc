val a = Map(
  1 -> 2.0,
  2 -> 5.1,
  3 -> 4.5
)

val b = Map(
  1 -> 2.0,
  2 -> 5.1,
  3 -> 4.5
)

def pearsonScore(a: Map[Int, Double], b: Map[Int, Double]): Option[Double] = {

  assert(a.size == b.size, "Sizes of both Maps are not equal")
  val n = a.size
  // add up all the preferences
  val sum1 = a.values.sum
  val sum2 = b.values.sum

  val listOfCommonKeys = a.keySet.intersect(b.keySet).toSeq
  //  println(listOfCommonKeys)

  //  println(sum1, sum2)

  // sum up the squares
  val sum1Sq = a.values.foldLeft(0.0)(_ + Math.pow(_, 2))
  val sum2Sq = b.values.foldLeft(0.0)(_ + Math.pow(_, 2))

  println(sum1Sq, sum2Sq)

  // sum up the products
  val pSum = listOfCommonKeys.foldLeft(0.0)((accum, element) => accum + a(element) * b(element))
  println(pSum)
  //
  //  // calculate the pearson score
  val numerator = pSum - (sum1 * sum2 / n)
  val denominator = Math.sqrt((sum1Sq - Math.pow(sum1, 2) / n) * (sum2Sq - Math.pow(sum2, 2) / n))
  if (denominator == 0) None else Some(numerator / denominator)


}

println(pearsonScore(a, b).get)