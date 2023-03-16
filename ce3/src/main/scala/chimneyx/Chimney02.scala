package chimneyx

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.partial._

object Chimney02 extends App {

  case class UserForm(name: String, ageInput: String, email: Option[String])
  case class User(name: String, age: Int, email: String)

  UserForm("John", "21", Some("john@example.com"))
    .intoPartial[User]
    .withFieldComputedPartial(_.age, form => Result.fromCatching(form.ageInput.toInt))
    .transform
    .asOption // Some(User("name", 21, "john@example.com"))

  val result = UserForm("Ted", "eighteen", None)
    .intoPartial[User]
    .withFieldComputedPartial(_.age, form => Result.fromCatching(form.ageInput.toInt))
    .transform

  result.asOption // None
  pprint.pprintln(result.asErrorPathMessageStrings)

}
