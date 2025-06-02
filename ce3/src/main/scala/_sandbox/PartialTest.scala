package _sandbox

import cats.implicits._
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import java.io.IOException

class PartialTest extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks with Inside {

  case class Person(age: Int, name: String) {
    import Person._

    val k = x
  }

  object Person {
    val x = 5
  }

  val p1 = Person(33, "Jim")
  val p2 = Person(35, "Jim")

  // Java-ish static design
//  p1.x

  // 2004
  val z = Person.x

  def twice(x: Int): Int = x * 2
  def inverse(x: Int): Int = 1 / x
  def f1(x: Double): Double = 1 / x

  test("1x") {


    def fold[A, B, C](a: A, f: (A, B) => A, g: A => C, xs: List[B]) = ???

//    def map[A, B](f: A => B)(a: A): B = ???




    def min(xs: List[Int]): Option[Int] = ???

    val m1 = min(List(1,2,3))

    m1 match {
      case Some(value) => ???
      case None => ???
    }

    sealed trait ErrorMessage


    def readline: Either[ErrorMessage, Option[String]] = ???




























    val m: Map[String, Int] = List("a", "a", "b")
      .groupMapReduce(identity)(_ => 1)(_ + _)

    val map1 = Map("a" -> 1, "b" -> 2, "c" -> 3)
    val map2 = Map("a" -> -1, "b" -> -1)
    val map = map1 |+| map2 // Map("b" -> 1)
    pprint.log(map)

    val x = map.forall { case (_, x) => x > 0 }
  }

  test("1") {
    val y = inverse(0)
    pprint.log(y)
  }

  def inverse1(x: Double): Option[Double] =
    if (x == 0.0) None else Some(1 / x)

  def inverse2(x: Double): Either[String, Double] =
    if (x == 0.0) Left("function inverse is not defined at x=0") else Right(1 / x)

  test("2") {
    val q = inverse1(5)
    val w = inverse1(0)
    pprint.log(q)
    pprint.log(w)
  }

  case class User(age: Int, name: String)
  def makeUser(age: Int, name: String): Either[String, User] =
    if (age < 0) Left("age should be > 0")
    else if (name.length < 2) Left("name should be longer than 2")
    else Right(User(age, name))

  val inverse3: PartialFunction[Double, Double] = {
    case x if x != 0 => 1 / x
  }

  def nonZero(x: Double): Boolean = x != 0
  object NonZero {
    def unapply(x: Double): Option[Double] = Option(x).filter(nonZero)
  }

  val inverse4: PartialFunction[Double, Double] = {
    case NonZero(x) => 1 / x
  }

  def inverse8(x: Double): Either[String, Double] =
    inverse4.lift(x) match {
      case Some(y) => Right(y)
      case None => Left(s"function inverse is not defined at $x")
    }

  test("3") {
    val x = inverse4(5)
    val y = inverse4(0)
  }

  {
    val pf1: PartialFunction[Any, String] = { case x: Int => s"Int: $x" }
    val pf2: PartialFunction[Any, String] = { case x: String => s"String: $x" }
    val pf3: Any => String = { x: Any => s"Something else: $x" }

    val partialHandler = pf1 orElse pf2
    def fullHandler(x: Any): String =
      partialHandler.applyOrElse(x, pf3)

  }

  test("4") {
    val check1 = inverse4.isDefinedAt(5) // true
    val check2 = inverse4.isDefinedAt(0) // false
    pprint.pprintln(check1)
    pprint.pprintln(check2)
  }

  val inverse5: Double => Option[Double] = inverse4.lift

  val inverse6: PartialFunction[Double, Double] = inverse5.unlift

  test("5.orElse") {

    val handle1: PartialFunction[Int, String] = {
      case 1 => "one"
      case 2 => "two"
    }

    val handle2: PartialFunction[Int, String] = {
      case 3 => "three"
      case 4 => "four"
      case 5 => "five"
    }

    val handle3: PartialFunction[Int, String] = {
      case 10 => "ten"
      case 11 => "eleven"
      case 12 => "twelve"
    }

    val handle123: PartialFunction[Int, String] = handle1 orElse handle2 orElse handle3

    val r1 = handle123(1)
    val r3 = handle123(3)
    val r6 = handle123(6)

  }

  trait Test123[A, B, C] {
    def f1: PartialFunction[A, B]
    def f2: PartialFunction[B, C]
    def f3: PartialFunction[A, C] = f1.andThen(f2)
    def f4: PartialFunction[A, C] = f2.compose(f1)
  }

}
