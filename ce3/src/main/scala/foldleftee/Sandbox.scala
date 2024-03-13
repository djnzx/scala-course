package foldleftee

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

object Sandbox {

  /** foldLeft with Early Exit */
  def foldLeftEE[A, B](z: B)(f: (B, A) => (B, Boolean))(xs: => Seq[A]): B = {

    def go(xs: => Seq[A], acc: B): B = xs match {
      case Seq() => acc
      case _     =>
        val (acc2, exit) = f(acc, xs.head)
        if (exit) {
          pprint.log("exiting early ax x="->xs.head)
          acc2
        }
        else go(xs.tail, acc2)
    }

    go(xs, z)
  }

}

class SandboxSpec extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  import Sandbox._

  test("foldLeft with Early Exit") {

    val x: Int = foldLeftEE[Int, Int](0) { case (acc, x) =>
      if (acc > 10) (acc, true)
      else (acc + x, false)
    }(1 to 1000)

    pprint.log(x)

  }

}
