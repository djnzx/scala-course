package request.dto.auth.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class UserLoginRq(
  email: String,
  password: String
)

object UserLoginRq {
  implicit val rw: RW[UserLoginRq] = macroRW
}