package interview.general

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

trait Syntax2 {

  case class Check(f: Int => Boolean, msg: Int => String) {
    def unapply(x: Int): Option[String] = Option.when(f(x))(msg(x))
  }

  object Even extends Check(_ % 2 == 0, x => s"$x is Even")
  object Odd extends Check(_ % 2 != 0, x => s"$x is Odd")

}

trait Syntax1 {

  object Even {
    def unapply(x: Int): Option[String] =
      Option.when(x % 2 == 0)(s"$x is Even")
  }

  object Odd {
    def unapply(x: Int): Option[String] =
      if (x % 2 != 0) Some(s"$x is Odd") else None
  }

}

class TaskUnapplySpec extends AnyFunSpec with Matchers with Syntax2 {

  def go(x: Int): String = x match {
    case Even(msg) => msg
    case Odd(msg)  => msg
  }

  it("5") {
    go(5) shouldEqual "5 is Odd"
  }

  it("4") {
    go(4) shouldEqual "4 is Even"
  }

}
