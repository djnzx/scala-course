package request.dto.auth.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class ForgotPasswordRq(
  email: String,
  phoneNumber: String
)

object ForgotPasswordRq {
  implicit val rw: RW[ForgotPasswordRq] = macroRW
}
