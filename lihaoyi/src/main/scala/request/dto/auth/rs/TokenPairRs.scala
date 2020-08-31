package request.dto.auth.rs

import upickle.default.{macroRW, ReadWriter => RW}

case class TokenPairRs(
  act_token: String,
  user_Id: String,
  ep_enabled: Boolean,
  refresh_token: String
)

object TokenPairRs {
  implicit val rw: RW[TokenPairRs] = macroRW
}

