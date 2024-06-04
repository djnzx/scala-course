package cats101.validated

import cats.data.Validated
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.either._

object C158ValidatedExercise extends App {

  type FailFast[A] = Either[List[String], A]
  type FailSlow[A] = Validated[List[String], A]

  val FNAME = "name"
  val FAGE = "age"

  type FormData = Map[String, String]
  val webForm: FormData = Map(
    FNAME -> "Alex",
    FAGE -> "44"
  )

  case class User(name: String, age: Int)

  def getValue(name: String)(data: FormData)(emsg: => String) =
    data.get(name).toRight(List(emsg))
  val readName = getValue(FNAME) _
  val readAge = getValue(FAGE) _

  def valIsNotEmpty(emsg: => String)(s: String) =
    Right(s).ensure(List(emsg))(_.nonEmpty)
  def toNumber(emsg: => String)(s: String) =
    Either.catchOnly[NumberFormatException](s.toInt).leftMap(_ => List(emsg))
  def valIsValidNumber(emsg: => String)(n: Int) =
    Right(n).ensure(List(emsg))(_ > 0)

  def name(form: FormData) = readName(form)("name should be defined")
    .flatMap(valIsNotEmpty("name should be non blank"))
  def age(form: FormData) = readAge(form)("age should be defined")
    .flatMap(toNumber("age should be a number"))
    .flatMap(valIsValidNumber("age should be non negative"))

  def validateAndCreate(form: FormData) = (
    Validated.fromEither(name(form)),
    Validated.fromEither(age(form))
    ).mapN(User.apply).toEither

  val result: Either[List[String], User] = validateAndCreate(webForm)
  print(result)
}
