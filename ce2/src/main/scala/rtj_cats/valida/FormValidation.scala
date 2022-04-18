package rtj_cats.valida

import cats.data._
import cats.implicits._

object FormValidation extends App {

  type Form = Map[String, String]

  def getField(form: Form, fieldName: String): ValidatedNel[String, String] =
    form.get(fieldName).toValid(s"field `$fieldName` should exist").toValidatedNel

  def validateNonBlank(value: String, fieldName: String): ValidatedNel[String, String] =
    Validated.cond(value.nonEmpty, value, s"field `$fieldName` should be non empty").toValidatedNel

  def validateName(name: String): ValidatedNel[String, String] =
    Validated.cond(name(0).isTitleCase, name, "Name should start from capital").toValidatedNel

  def validateEmail(email: String): ValidatedNel[String, String] =
    Validated.cond(email.contains("@"), email, "Email should contain @").toValidatedNel

  def validatePassword(password: String): ValidatedNel[String, String] =
    Validated.cond(password.length >= 10, password, "Password should be longer than 10 characters").toValidatedNel

  def validateForm(form: Form): ValidatedNel[String, Form] = {
    val q1 = "name" match {
      case n =>
        getField(form, n)
          .andThen(x => validateNonBlank(x, n))
          .andThen(x => validateName(x))
    }

    val q2 = "email" match {
      case n =>
        getField(form, n)
          .andThen(x => validateNonBlank(x, n))
          .andThen(x => validateEmail(x))

    }

    val q3 = "password" match {
      case n =>
        getField(form, n)
          .andThen(x => validateNonBlank(x, n))
          .andThen(x => validatePassword(x))
    }

    (q1, q2, q3).tupled
      .as(form)
  }

  val form = Map(
    "name"     -> "Jim",
    "email"    -> "",
    "password" -> ""
  )

  val validated = validateForm(form).leftMap(_.toList)
  pprint.pprintln(validated)
}
