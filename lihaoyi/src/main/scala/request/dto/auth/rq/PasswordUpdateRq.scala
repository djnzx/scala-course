package request.dto.auth.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class PasswordUpdateRq(
  password: String // min 8
)

object PasswordUpdateRq {
  implicit val rw: RW[PasswordUpdateRq] = macroRW
}