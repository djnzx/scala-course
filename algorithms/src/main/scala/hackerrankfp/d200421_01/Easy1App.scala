package hackerrankfp.d200421_01

/**
  * https://www.hackerrank.com/challenges/area-under-curves-and-volume-of-revolving-a-curv/problem?isFullScreen=true
  */
object Easy1App extends App {

  def readLine = scala.io.StdIn.readLine()

  val STEP = 0.001
  val STEPW = (1/STEP).toInt

  def pow(x: Double, n: Int): Double = {
    val v = (1 to scala.math.abs(n)).foldLeft(1: Double)((acc, _) => acc * x)
    if      (n > 0) v
    else if (n < 0) 1/v
    else 1
  }

  val square = (v: Double) => v*STEP
  val volume = (v: Double) => pow(v, 2)*scala.math.Pi*STEP

  def valueAt(ks:List[Int], ps: List[Int], x:Double)(fx: Double => Double): Double =
    fx((ks zip ps).foldLeft(0: Double) { (acc, el) => acc + el._1*pow(x, el._2) })

  def f(ks:List[Int], ps: List[Int], x:Double): Double =
    valueAt(ks, ps, x)(square)

  def area(ks:List[Int], ps: List[Int], x:Double): Double =
    valueAt(ks, ps, x)(volume)

  def range(l: Int, r: Int): Seq[Double] =
    (l*STEPW to r*STEPW).map(_.toDouble*STEP).toVector

  def summation(func: (List[Int], List[Int], Double) => Double,
                upperLimit: Int, lowerLimit: Int,
                coefficients: List[Int], powers: List[Int]): Double =
    range(lowerLimit, upperLimit).foldLeft(0: Double) { (acc, x) => acc + func(coefficients, powers, x) }

  val r1 = summation(   f, 20, 2, List(1,2), List(0,1) )
  val r2 = summation(area, 20, 2, List(1,2), List(0,1) )
  println(r1)
  println(r2)
}
