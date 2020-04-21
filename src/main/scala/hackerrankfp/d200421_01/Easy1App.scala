package hackerrankfp.d200421_01

/**
  * https://www.hackerrank.com/challenges/area-under-curves-and-volume-of-revolving-a-curv/problem?isFullScreen=true
  */
object Easy1App extends App {

  def readLine = scala.io.StdIn.readLine()

  def pow(x: Double, n: Int): Double = {
    val v = (1 to scala.math.abs(n)).foldLeft(1: Double)((acc, _) => acc * x)
    if      (n > 0) v
    else if (n < 0) 1/v
    else 1
  }

  def f(coefficients:List[Int], powers: List[Int], x:Double): Double =
    (coefficients zip powers)
      .foldLeft(0: Double) { (acc, el) => acc + el._1*pow(x, el._2)*0.001 }

  def area(coefficients:List[Int], powers: List[Int], x:Double): Double =
    pow(f(coefficients, powers, x)*1000, 2)*scala.math.Pi/1000

  def range(l: Int, r: Int): Seq[Double] =
    (l*1000 to r*1000).map(_.toDouble/1000).toVector

  def summation(func: (List[Int], List[Int], Double) => Double,
                 upperLimit: Int, lowerLimit: Int,
                 coefficients: List[Int], powers: List[Int]
               ): Double = {
    val minmax: Seq[Double] = range(lowerLimit, upperLimit)
    minmax.foldLeft(0: Double) { (acc, x) => acc + func(coefficients, powers, x) }
  }

  val r1 = summation(   f, 20, 2, List(1,2), List(0,1) )
  val r2 = summation(area, 20, 2, List(1,2), List(0,1) )
  println(r1)
  println(r2)

}
