package x00topics.splat_destruct

// http://www.lorrin.org/blog/2011/10/04/scalas-missing-splat-operator/
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

  // call way 3. by unpacking tuple
  val (p1, p2) = arg
  val result3: Double = f(p1, p2)
  println(result3)

  // call way 4. by automatic unpacking
  val result4: Double = f _ tupled arg
  println(result4)

  val argsq: Seq[AnyVal] = Seq(2, 3.5)

  import shapeless._
  import HList._
  import syntax.std.traversable._

  val x: Seq[AnyVal] = List(1, 2, 3)
  val y: Option[Int :: Double :: HNil] = x.toHList[Int::Double::HNil]
  // actually unsafe operation
  val z: (Int, Double) = y
    .get    // unpack option to HList
    .tupled // convert HList to tuple

  // seq to list
  val result5: Double = f _ tupled z
  println(result5)

}
