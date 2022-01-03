package rtj.valida

import cats.data.NonEmptyList
import cats.data.Validated
import cats.data.ValidatedNel
import cats.implicits._

object ValidaApp extends App {

  val valid: Validated[Nothing, Int] = Validated.valid(30)
  val invalid = Validated.invalid("bad1")

  val v1: Validated[String, Int] = 30.valid[String]
  val v2: Validated[NonEmptyList[String], Int] = 30.validNel[String]
  val v3: ValidatedNel[String, Int] = 30.validNel[String]

  val v1a: ValidatedNel[String, Int] = v1.toValidatedNel

}
