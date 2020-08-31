package request.dto.auth.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class RefreshTokenRq(refresh_token: String)

object RefreshTokenRq {
  implicit val rw: RW[RefreshTokenRq] = macroRW
}
