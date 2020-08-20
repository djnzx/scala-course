package composition

import cats.implicits.{
  catsKernelStdGroupForInt, 
  catsKernelStdMonoidForString
}
import cats.{Monoid, Semigroup}

object Playground1 extends App {
  /**
    * it has:
    * - type
    * - operation (combine):
    *   combine: (A, A) => A
    * - identity: Id: () => A
    *     - combine(A, Id) = A
    *     - combine(ID, A) = A
    */
  val mi: Monoid[Int] = Monoid[Int]
  val empty: Int = mi.empty
  val combined: Int = mi.combine(1,2)
  /**
    * why Monoid[Int] implemented as addition? ...
    */
  
  pprint.pprintln(combined)
  val si: Semigroup[Int] = Semigroup[Int]
  si.combine(1,2)
}

object Playground2 extends App {
  
  def appendLog[A: Monoid](was: A, s: A): A = Monoid[A].combine(was, s)
  
  def add1(a: Int): (Int, String) = {
    val a1 = a + 1
    val log = s"$a + 1 = $a1" 
    (a1, log)
  }
  
  val listWithLog: List[(Int, String)] = List(
    add1(5),
    add1(10),
    add1(100),
  )
  
  // sum the result, and concatenate the log to String
  def collectTo[A: Monoid, B](as: List[(A, B)])(implicit mb: Monoid[B]): (A, B) = {
    val ma = Monoid[A]
    as.foldLeft((ma.empty, mb.empty)) { case ((sum, list), (a, b) ) => {
      (ma.combine(sum, a), mb.combine(list, b))
    }}}
  
  val r1: (Int, String) = collectTo(listWithLog)
  pprint.pprintln(r1)
  
  val mls: Monoid[List[String]] = new Monoid[List[String]] {
    override def empty: List[String] = List.empty
    override def combine(x: List[String], y: List[String]): List[String] = x ++ y
  }
  // sum the result, and concatenate the log to List[String]
  
  
//  pprint.pprintln(r2)
  
  
}