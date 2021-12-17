package request.dto.auth.rs

import upickle.default.macroRW
import upickle.default.{ReadWriter => RW}

case class AccessTokenRs(
    act_token: String)

object AccessTokenRs {
  implicit val rw: RW[AccessTokenRs] = macroRW[AccessTokenRs]
}
