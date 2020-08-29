package request

import request.dto.{LoginRq, RegRq}
import requests.Response

object ActApi extends App {
  val urlBase    = "http://aacctt.ddns.net:19901"
  val urlAuth    = s"$urlBase/auth-service"
  val headers = Seq(
    "Content-Type"->"application/json",
    //    "Accept"->"*/*",
    //    "Accept-Encoding"->"gzip, deflate, br"
  )
  
  val email = "a77x77@gmail.com"
  val password = "abc123DEF"
  
  def registerNewUser = {
    val urlAuthReg = s"$urlAuth/ss/account/register"
    val regRq = RegRq(email, "alexr1", "r", "123-45-67", "M", "iOS13", "121212", "A", "B", password)
    val json: String = upickle.default.write(regRq)
    val r: Response = requests.post(urlAuthReg, headers = headers, data = json)
    pprint.log(r)
    /**
      * "act_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3MDUiLCJleHAiOjE1OTg0Nzc5NDEsImlhdCI6MTU5ODQ3NDM0MX0.75JRrMi-v8ZM75dO8C6oSeX-FmbI7Q9BlsbufnO2_CQ",
      * "user_Id": "0a68781a-792a-427c-8f7a-12171966a807",
      * "ep_enabled": false,
      * "refresh_token": "M1lSzZawwP3frINhysgEN0qnmkqpBhNK85hoioy0yqphLCCfj1dhSTn3yaWWfhIyemcLb1raD4zSmqrBQvsnwuyXLf1SGEz0lDKQS5SIFnXEiFTXMcDYC0su4zey9smb"
      */
  }
  
  def login = {
    val urlLogin   = s"$urlAuth/ss/account/login"
    val loginRq = LoginRq(email, password)
    val json = upickle.default.write(loginRq)
    val r: Response = requests.post(urlLogin, headers = headers, data = json)
    pprint.log(r)
    /**
      * {
      *   "act_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3MDUiLCJleHAiOjE1OTg1NjIyNDksImlhdCI6MTU5ODU1ODY0OX0.gUf7jsegnwDMS-Z79XimqksnibxKezAjr8do4Zny6Zs",
      *   "user_Id":"0a68781a-792a-427c-8f7a-12171966a807",
      *   "ep_enabled":false,
      *   "refresh_token":"FHpo0en9e0up32MO6vOnIDGOEmyVzBXtMscSMTePCKraW9DhGkL3JzdpPfiruPLv8BVDpbL7zl6wdhxE1du8V1wsJHlIlgHBp5mDjzfhgmeJvkf9rYWYJhL2lEeWiuFp"
      * }
      */
    
  }
  
  login
  
}
