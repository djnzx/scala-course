package request.dto.auth.rs

import upickle.default.{macroRW, ReadWriter => RW}

case class UserInfoRs(
  firstName: String,
  lastName: String,
  email: String,
  userId: String,
  expiryPassWord: Boolean,
  phoneNumber: String,
  sex: String,
  deviceOS: String,
  deviceUniqueID: String,
  dob: String,
  profileImage: String,
)

object UserInfoRs {
  implicit val rw: RW[UserInfoRs] = macroRW
}
