package request.dto.auth.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class UpdateUserRq(
  firstName: String, // min 2
  lastName: String, // min 2
  email: String, // not null
  phoneNumber: String, // min 6
  sex: String,
  dob: String
)

object UpdateUserRq {
  implicit val rw: RW[UpdateUserRq] = macroRW
}
