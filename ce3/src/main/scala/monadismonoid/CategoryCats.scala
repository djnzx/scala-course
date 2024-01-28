package monadismonoid

import cats._
import cats.arrow._
import cats.data._
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object CategoryCats {

  class Box1[A] {
    // combine
    val a: Semigroup[A] = ???
    // + empty
    val b: Monoid[A] = ???
  }

  class Box2[F[_]] {
    val x0: Invariant[F] = ???
    val x1: Functor[F] = ???
    val x2: Apply[F] = ???
    val x3: Applicative[F] = ???
    val x4: FlatMap[F] = ???
    val x5: Monad[F] = ???
  }

  object Composition {
    val inc: Int => Int = (x: Int) => x + 1
    val doToInt: Double => Int = (x: Double) => x.toInt
    val intToS: Int => String = (x: Int) => x.toString

    val f1: Int => String = inc.rmap(intToS)
    val f2: Double => Int = inc.lmap(doToInt)
    val f3: Double => String = inc.dimap(doToInt)(intToS)
  }

}


class CategoryCatsSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

}
