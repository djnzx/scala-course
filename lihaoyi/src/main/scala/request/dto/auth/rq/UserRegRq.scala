package request.dto.auth.rq

import java.util.UUID

import upickle.default.{macroRW, ReadWriter => RW}

case class UserRegRq(
  email:          String, // NN
  firstName:       String, // NN 
  lastName:       String,
  phoneNumber:    String,
  sex:            String,
  deviceOS:       String, // NN 
  deviceUniqueID: String,
  dob:            String,
  profileImage:    String,
  password:       String, // NN
)

object UserRegRq {
  implicit val rw: RW[UserRegRq] = macroRW
  
  def test = {
    val name = UUID.randomUUID.toString
    val email = s"test.$name@gmail.com"
    val password = s"$name-QAZ"
    
    UserRegRq(email, name, "r", "123-45-67", "M", "iOS13", "121212", "A", "B", password)
  }

  
}
