package x0lessons.splat_destruct

object DestructApp01 extends App {
  // full syntax
  def g1(xs:Int*) = xs.foldLeft[Int](0)((a: Int, b: Int) => a + b)
  // shortened with inferred result type
  def g2(xs:Int*) = xs.foldLeft(0)((a: Int, b: Int) => a + b)
  // shortened with inferred result type and parameter for behavior
  def g3(xs:Int*) = xs.foldLeft(0)((a, b) => a + b)
  // shortened with inferred result type and parameter for behavior
  def g4(xs:Int*) = xs.foldLeft(0)(_ + _)

  println(g1())                    // 0
  println(g2(1))              // 1
  println(g3(1, 2, 3))        // 6
  println(g4(Seq(1, 2, 3, 4): _*)) // 10

  // fixed arity syntax
  def f(x:Int, y:Double): Double = x * y

  // call way 1. by specifying arguments
  val result1: Double = f(2, 3.5)
  println(result1)

  val arg = (2, 3.5)
  // call way 2. by unpacking tuple
  val result2: Double = f(arg._1, arg._2)
  println(result2)

  // call way 3. by automatic unpacking
  val result3: Double = f _ tupled arg
  println(result3)

}
