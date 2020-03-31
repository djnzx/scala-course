package cats

import cats.data.Validated
import cats.syntax.either._
import cats.syntax.validated._

object C158ValidatedExercise extends App {

  val FNAME = "name"
  val FAGE = "age"

  val formFromWeb: Map[String, String] = Map(
    FNAME -> "Alex",
    FAGE -> "40"
  )

  case class User(name: String, age: Int)

  val readName: Map[String, String] => Option[String] =
    (form: Map[String, String]) => form.get(FNAME)
  val readAge: Map[String, String] => Option[String] =
    (form: Map[String, String]) => form.get(FAGE)
  /**
    * the name and age must be specified;
    * the name must not be blank;
    * the age must be a valid non-negative integer.
    */
  def validate(name: Option[String], age: Option[String]): Validated[List[String], User] = {


    // validated to combine
    // either to fail-fast
    ???
  }

  val result: Either[List[String], User] =
    validate(readName(formFromWeb), readAge(formFromWeb))
      .toEither

  print(result)
}
