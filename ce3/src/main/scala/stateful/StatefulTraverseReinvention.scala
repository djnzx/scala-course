package stateful

import cats._
import cats.data._
import cats.implicits._
import fs2.Pure
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object StatefulTraverseReinvention {

  object ideas {

    /** let's say we have stateful mapping,
      * which always takes state (could be empty if we say {{{S <:< Option[S']}}})
      * from previous step and returns modified state for the next mapping
      * {{{f: (S, A) => (S, B)}}}
      */
    def f[A, B, S](s: S, a: A): (S, B) = ???

    /** we can lift it to */
    def mapState[A, B, S](a: A): State[S, B] = State(s => f[A, B, S](s, a))

    def initialState[S]: State[S, Unit] = State.pure[S, Unit](())
    def initialValue[S]: S = ???.asInstanceOf[S]

    /** then having Traversable[A] */

  }

  object plain {

    def statefulFold[A, B, S](f: (S, A) => (S, B)) = (xs: List[A]) =>
      (s0: S) =>
        xs.foldLeft(s0 -> List.empty[B]) { case ((s, bs), a) =>
          val (s1, b) = f(s, a)
          s1 -> (b :: bs)
        } match {
          case (s, bs) => s -> bs.reverse
        }

  }

}

class StatefulTraverseReinvention extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  /** our state type */
  type ST = Map[Int, String]
  val st0 = Map.empty[Int, String]

  /** our real types: A = Int, B = Double */

  /** our plain function: (S, A) => (S, B) */
  def fab(s: ST, a: Int): (ST, Double) = {
    val s2 = s + (a -> (a * 100).toString)
    val b = (a + 1000).toDouble
    (s2, b)
  }

  /** our function lifted to State[S, A] */
  def fasb(a: Int): State[Map[Int, String], Double] = State(s => fab(s, a))

  test("plain, manual traverse, reverse, and state passing") {
    import StatefulTraverseReinvention.plain._

    val (s, bs) = statefulFold(fab)
      .apply(List(1, 2, 3))
      .apply(st0)

    bs shouldBe List(1001.0, 1002.0, 1003.0)
    s shouldBe Map(1 -> "100", 2 -> "200", 3 -> "300")
  }

  test("state monad: StateT[Eval, S, A") {
    val ssb: State[Map[Int, String], List[Double]] = List(1, 2, 3).traverse(fasb)
    val eval: Eval[(Map[Int, String], List[Double])] = ssb.run(st0)
    val (s, bs) = eval.value

    bs shouldBe List(1001.0, 1002.0, 1003.0)
    s shouldBe Map(1 -> "100", 2 -> "200", 3 -> "300")
  }

  test("state monad: cats Traverse.mapAccumulate") {
    val (s, bs) = List(1, 2, 3).mapAccumulate(st0)(fab)

    bs shouldBe List(1001.0, 1002.0, 1003.0)
    s shouldBe Map(1 -> "100", 2 -> "200", 3 -> "300")
  }

  test("state monad: StateT[Id, S, A] - if you are sure that will never overflow stack") {
    type StateId[S, A] = StateT[Id, S, A]
    object StateId {
      def apply[S, A](f: S => (S, A)): StateId[S, A] = IndexedStateT.applyF(Id((s: S) => Id(f(s))))
    }

    /** lifted to StateT[Id, S, A] */
    def asb(a: Int) = StateId((s: ST) => fab(s, a))

    val ss = List(1, 2, 3).traverse(asb)
    val (s, bs) = ss.run(st0)

    bs shouldBe List(1001.0, 1002.0, 1003.0)
    s shouldBe Map(1 -> "100", 2 -> "200", 3 -> "300")
  }

  test("fs2.Stream") {
    val as = fs2.Stream(1, 2, 3)
    val bss: fs2.Stream[Pure, (Map[Int, String], Double)] = as.mapAccumulate(st0)(fab)

    val bs: List[Double] = bss.map(_._2).compile.toList
    val s: Map[Int, String] = bss.map(_._1).last.compile.toList.flatten.headOption.getOrElse(st0)
    bs shouldBe List(1001.0, 1002.0, 1003.0)
    s shouldBe Map(1 -> "100", 2 -> "200", 3 -> "300")
  }

  test("fs2.scan - all intermediate steps") {
    val xs = fs2.Stream(1, 2, 3, 4)
      .scan(0)(_ + _)
      .toList

    pprint.pprintln(xs)
    xs shouldBe List(0, 1, 3, 6, 10)
  }

  test("fs2.fold - final result only") {
    val xs = fs2.Stream(1, 2, 3, 4)
      .fold(0)(_ + _)
      .toList

    pprint.pprintln(xs)
    xs shouldBe List(10)
  }

  test("pull") {
    val xs = fs2.Stream(1, 2, 3, 4)
    xs.pull
  }



}
