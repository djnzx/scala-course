package glovolive

object TaskCDynamicScala {
  
  def if_func[A](cond: Boolean, t: => A, f: => A) = if (cond) t else f
  
  def bToI2(b: Boolean) = if_func(b, 1, 0)
  
  def bToI(b: Boolean) = if (b) 1 else 0
  
  def hasSubArrayWithSum(xs: Array[Int], k: Int): Int =
    xs.foldLeft((Set.empty[Int], 0, 0)) { case ((parts, sum, cnt), item) =>
      val sum2 = sum + item
      val delta1 = sum2 == k
      val delta2 = parts.contains(sum2 - k)
      (parts + sum2, sum2, cnt + bToI(delta1) + bToI(delta2))
    }._3
  
  def containsZeroSubArray(xs: Array[Int]): Boolean = 
    hasSubArrayWithSum(xs, 0) > 0
}
