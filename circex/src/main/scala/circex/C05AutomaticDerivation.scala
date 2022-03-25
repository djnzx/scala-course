package circex

import io.circe.generic.extras.Configuration
import io.circe.syntax.EncoderOps

object C05AutomaticDerivation extends App {

  case class Details(userId: Int)

  /** unconfigurable */
  object Details extends io.circe.generic.AutoDerivation
  val details = Details(33)

  println(details.asJson) // default: "userId"

  case class User(userId: Int)
  implicit val c: Configuration = Configuration.default.withSnakeCaseMemberNames

  /** configurable */
  object User extends io.circe.generic.extras.AutoDerivation

  val user = User(22)
  println(user.asJson) // configured: "user_id"

}
