package monoid

import cats.Monoid
import cats.implicits.{catsKernelStdGroupForInt, catsKernelStdMonoidForList, catsKernelStdMonoidForString, catsKernelStdMonoidForVector}

object LoggingExample extends App {
  /**
    * we want to implement abstract logger
    */

  /**
    * our function
    * does the business
    * an returns a tuple of
    * two values (value, log_item)
    */
  def add1(a: Int): (Int, String) = {
    val a1 = a + 1
    val log = s"$a + 1 = $a1"
    (a1, log)
  }

  def appendLog[ACC, A](left: ACC, right: A)(implicit ma: Monoid[ACC], f: A => ACC): ACC =
    ma.combine(left, f(right))
  def appendLog[ACC](left: ACC, right: ACC)(implicit ma: Monoid[ACC]): ACC =
    ma.combine(left, right)

  val data: List[(Int, String)] = List(
    add1(5),
    add1(10),
    add1(100),
  )

  /** this is an implementation */
  def collectTo[A, B, C](as: List[(A, B)])(f: B => C)(implicit ma: Monoid[A], mc: Monoid[C]): (A, C) =
    as.foldLeft((ma.empty, mc.empty)) { case ((sum, list), (a, b)) => {
      (ma.combine(sum, a), mc.combine(list, f(b)))
    }}

  /** collect just to string */
  val r1: (Int, String) = collectTo(data)(identity)

  /** collect to list */
  val r2: (Int, List[String]) = collectTo(data)(List(_))

  /** collect to vector */
  val r3: (Int, Vector[String]) = collectTo(data)(Vector(_))


  val intSumMonoid: Monoid[Int] = Monoid[Int]
  val stringCSMonoid: Monoid[String] = new Monoid[String] {
    override def empty: String = ""
    override def combine(x: String, y: String): String = if (x.isEmpty) y else s"$x, $y"
  }

  /** string, comma separated */
  val r4: (Int, String) = collectTo(data)(identity)(intSumMonoid, stringCSMonoid)

  pprint.log(r1)
  pprint.log(r2)
  pprint.log(r3)
  pprint.log(r4)
}
