package _playground

import cats.data.Validated
import cats.implicits._
import io.circe.syntax.EncoderOps

object ValidationPlayground extends App {

  sealed trait E
  case object AgeShouldBeAnumber extends E
  case object AgeShouldBeNonNegative extends E
  case object AgeShouldBelessThan150 extends E

  def validateAge2(raw: String): Validated[List[E], Int] =
    raw.valid
      .ensure(AgeShouldBeAnumber)(s => s.toIntOption.isDefined)
      .map(s => s.toInt)
      .ensure(AgeShouldBeNonNegative)(s => s >= 0)
      .ensure(AgeShouldBelessThan150)(s => s < 150)
      .leftMap(_.pure[List])

  def validateAge(raw: String): Validated[List[String], Int] =
    raw.valid
      .ensure("age should be a number")(s => s.toIntOption.isDefined)
      .map(s => s.toInt)
      .ensure("age should be non negative")(s => s >= 0)
      .ensure("age should be less than 150")(s => s < 150)
      .leftMap(_.pure[List])

  def validateAgeEx(raw: String): Int =
    validateAge(raw) match {
      case Validated.Valid(a)         => a
      case Validated.Invalid(message) => throw new IllegalArgumentException(message.mkString("\n"))
    }

  def validateName(raw: String): Validated[List[String], String] =
    raw.valid
      .ensure("name should be non empty")(s => s.nonEmpty)
      .ensure("name should start from character")(s => s(0).isLetter)
      .ensure("name should contain no special characters")(s => s.forall(c => c.isLetterOrDigit))
      .leftMap(_.pure[List])

  def validateNameEx(raw: String): String =
    validateName(raw) match {
      case Validated.Valid(a)         => a
      case Validated.Invalid(message) => throw new IllegalArgumentException(message.mkString("\n"))
    }

//  val r1x: Int = validateAgeEx("bla-bla") // Invalid(should be a number)
//  println(r1x)

  val r2 = validateAge("13") // Valid(13)
//
//  val r3 = validateAge("200") // Invalid(should be less than 150)
//  println(r3)
//
//  val r4 = validateAge("-1200") // Invalid(should be non negative)
//  println(r4)

  val n1 = validateName("")   // Invalid(should be non empty)
  val n2 = validateName("1")  // Invalid(should start from character)
  val n3 = validateName("a-") // Invalid(should contain no special characters)

  val a1: Validated[List[String], Int]    = validateAge("bla-bla") // Invalid(should be a number)
  val n4: Validated[List[String], String] = validateName("-")      //    // Valid(jim)

  case class User(name: String, age: Int)

  val x: Validated[List[String], User] = (a1, n4).mapN { case (n, a) => User(a, n) }

  def validateUser(age: String, name: String): Validated[List[String], User] =
    (
      validateAge(age),
      validateName(name)
    ).mapN { case (a, n) => User(n, a) }

  def validateUserEx(age: String, name: String): User =
    validateUser(age, name) match {
      case Validated.Valid(a)   => a
      case Validated.Invalid(e) => throw new IllegalArgumentException(e.mkString("\n"))
    }

  val a = List(
    "a" -> "3.5",
    "b" -> "Value2"
  ).toMap.asJson

  println(a)

}
