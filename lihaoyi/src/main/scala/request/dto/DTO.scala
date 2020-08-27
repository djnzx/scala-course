package request.dto

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
  implicit val regRqRW: upickle.default.ReadWriter[RegRq] = upickle.default.macroRW
}

case class RegRs(act_token:     String,
                 user_Id:       String,
                 ep_enabled:    Boolean,
                 refresh_token: String)

/**
  * "act_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3MDUiLCJleHAiOjE1OTg0Nzc5NDEsImlhdCI6MTU5ODQ3NDM0MX0.75JRrMi-v8ZM75dO8C6oSeX-FmbI7Q9BlsbufnO2_CQ",
  * "user_Id": "0a68781a-792a-427c-8f7a-12171966a807",
  * "ep_enabled": false,
  * "refresh_token": "M1lSzZawwP3frINhysgEN0qnmkqpBhNK85hoioy0yqphLCCfj1dhSTn3yaWWfhIyemcLb1raD4zSmqrBQvsnwuyXLf1SGEz0lDKQS5SIFnXEiFTXMcDYC0su4zey9smb"
  */
  