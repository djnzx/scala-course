package phantom

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class BetterTyping1 extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  /** domain definition */
  trait Student
  trait Teacher
  case class Person[A](value: String)

  def teach(whom: Person[Student]) = ()
  def learn(from: Person[Teacher]) = ()

  /** data */
  val jim = Person[Student]("Jim")
  val bill = Person[Teacher]("William")

  test("1") {

    /** good */
    teach(jim)

    /** will not compile */
//    teach(bill)

    /** good */
    learn(bill)

    /** will not compile */
//    learn(jim)
  }

}
