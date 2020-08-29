package request.dto
import upickle.default.{ReadWriter => RW, macroRW}

case class RegRq(email: String,    // NN
                 firstName: String, // NN 
                 lastName: String, 
                 phoneNumber: String, 
                 sex: String,
                 deviceOS: String, // NN 
                 deviceUniqueID: String, 
                 dob: String, 
                 profileImage: String,
                 password: String, // NN
                )
object RegRq {
  implicit val rw: RW[RegRq] = macroRW
}

case class RegRs(act_token:     String,
                 user_Id:       String,
                 ep_enabled:    Boolean,
                 refresh_token: String)

case class LoginRq(email: String, password: String)
object LoginRq {
  implicit val rw: RW[LoginRq] = macroRW
}
  