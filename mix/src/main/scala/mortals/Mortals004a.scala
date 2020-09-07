package mortals

object Mortals004a {
  /**
    * `final case class` also known as products
    * `sealed abstract class` also known as coproducts (Either)
    * `case object` and Int, Double, String (etc) - values
    * 
    * We prefer abstract class to trait 
    * in order to get better binary compatibility 
    * and to discourage trait mixing.
    * 
    * product:   ABC = a AND b AND c
    * coproduct: XYZ = x XOR y XOR z
    */

  // values
  case object A
  type B = String
  type C = Int
  
  // product
  final case class ABC(a: A.type, b: B, c: C)

  // coproduct
  sealed abstract class XYZ
  case object X extends XYZ
  case object Y extends XYZ
  final case class Z(b: B) extends XYZ

  sealed abstract class Foo
  final case class Bar(flag: Boolean) extends Foo
  final case object Baz extends Foo
  def thing(foo: Foo) = foo match { 
    case Bar(_) => true
  }

  type |:[L,R] = Either[L, R]
  type Accepted = String |: Long |: Boolean
  // or
  sealed abstract class Accepted2
  final case class AcceptedString(value: String) extends Accepted2
  final case class AcceptedLong(value: Long) extends Accepted2
  final case class AcceptedBoolean(value: Boolean) extends Accepted2
  // https://github.com/propensive/totalitarian
  
  import eu.timepit.refined
  import refined.api.Refined
  import refined.numeric.Positive
  import refined.collection.NonEmpty
  
  final case class Person(
    name: String Refined NonEmpty,
    age: Int Refined Positive
  )

  // Prefer Coproduct over Product
  // good
  implicit class DoubleOps1(x: Double) {
    def sin: Double = java.lang.Math.sin(x)
  }
  // better
  implicit final class DoubleOps2(private val x: Double) extends AnyVal { 
    def sin: Double = java.lang.Math.sin(x)
  }

//  import simulacrum._
//  @typeclass trait Ordering[T] {
//    def compare(x: T, y: T): Int
//    @op("<") def lt(x: T, y: T): Boolean = compare(x, y) < 0
//    @op(">") def gt(x: T, y: T): Boolean = compare(x, y) > 0
//  }
//  @typeclass trait Numeric[T] extends Ordering[T] { 
//    @op("+") def plus(x: T, y: T): T
//    @op("*") def times(x: T, y: T): T 
//    @op("unary_-") def negate(x: T): T
//    def zero: T
//    def abs(x: T): T = if (lt(x, zero)) negate(x) else x
//  }
//  import Numeric.ops._
//  def signOfTheTimes[T: Numeric](t: T): T = -(t.abs) * t

  /**
    * https://github.com/scala/bug/issues/9670
    */
  /**
    * Avoid using java.net.URL at all costs:
    * it uses DNS to resolve the hostname part when performing 
    * toString, equals or hashCode.
    */
}
