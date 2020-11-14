package akkahttp.tools

import akkahttp.service.UserService.ActionPerformed
import akkahttp.service.{User, Users}
import spray.json.DefaultJsonProtocol

object JsonEncDec {

  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)
  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
