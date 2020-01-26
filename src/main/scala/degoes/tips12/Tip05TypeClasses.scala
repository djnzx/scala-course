package degoes.tips12

object Tip05TypeClasses extends App {

  // interface, bad approach
  trait NumberLike[A] { self =>
    def + (that: A): A
  }
  sealed case class Rational(n: BigInt, d:BigInt)
    extends NumberLike[Rational]
  {
    override def +(that: Rational): Rational = ???
  }
  // A is subtype of NumberLike[A]
  def sumAll[A <: NumberLike[A]](list: List[A]): A = ???
  val total: Rational = sumAll(List[Rational]())

  // solution: Type Class
  abstract class Numeric[A] {
    def add(l: A, r: A): A
  }
  object Numeric {
    def apply[A](implicit n: Numeric[A]): Numeric[A] = n
  }
  implicit class NumericSyntax[A](l: A) {
    def \/ (r: A)(implicit n: Numeric[A]): A = n.add(l, r)
  }
  implicit val rationalNumeric: Numeric[Rational] = new Numeric[Rational] {
    override def add(l: Rational, r: Rational): Rational = Rational(l.n + r.n, l.d + r.d)
  }
  // A must support implementation of Numeric for A class
  def sumAll2[A: Numeric](list: List[A]): A = ???
//  implicit val byteNumeric: Numeric[Byte] = (l: Byte, r: Byte) => ???
//  implicit val shortNumeric: Numeric[Short] = (l: Short, r: Short) => ???
  implicit val intNumeric: Numeric[Int] = (l: Int, r: Int) => l + r
  implicit val intDouble: Numeric[Double] = (l: Double, r: Double) => l + r
  // ...

  val z = 111 \/ 222
  val z2 = Rational(1, 2) \/ Rational(3, 4)
  println(z)
  println(z2)

  /**
    * problem:
    * Byte, Short, Int, Long, Float, Double don't extent NumberLike
    */
  // automatic derivation, recursion in types!
  implicit def tuple2numeric[A: Numeric, B: Numeric] = new Numeric[(A, B)] {
    override def add(l: (A, B), r: (A, B)): (A, B) = (l._1 \/ r._1, r._2 \/ r._2)
  }
  val t2: (Int, Int) = sumAll2((1,2) :: (2,3) :: (3,4) :: Nil)
  val t22: (Int, (Int, Double)) = sumAll2((1,(2, 1.5)) :: (2,(3, 2.5)) :: (3,(4,3.5)) :: Nil)

}
