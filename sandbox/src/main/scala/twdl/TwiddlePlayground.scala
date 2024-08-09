package twdl

import common.Base
import org.scalatest.Succeeded
import org.typelevel.twiddles._
import org.typelevel.twiddles.syntax._
import shapeless._

// https://github.com/typelevel/twiddles
object TwiddlePlayground {

  /** On Scala 3, twiddle lists are represented as generic tuples -- e.g.,
    * F[Int *: String *: Boolean *: EmptyTuple] or equivalently
    * F[(Int, String, Boolean)].
    *
    * On Scala 2, twiddle lists are represented as Shapeless heterogeneous lists.
    */

  val x = 1

}

class TwiddlePlayground extends Base {

  import TwiddlePlayground._

  test("1") {

    case class Foo(x: Int, y: String)

    val a = Option(42)
    val b = Option("Hi")

    val ab: Option[Int *: String *: EmptyTuple] = a *: b
    val foo: Option[Foo] = ab.to[Foo]

    val abh: Int :: String :: EmptyTuple = 42 :: "Hi" :: HNil

    pprint.log(ab)
    pprint.log(abh)
    pprint.log(foo)
  }

}
