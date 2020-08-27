package request

import request.dto.RegRq
import requests.{Response, connectTimeout}

object ActApi extends App {
  val urlBase    = "http://aacctt.ddns.net:19901"
  val urlAuth    = s"$urlBase/auth-service"
  val urlAuthReg = s"$urlAuth/ss/account/register"
  // POST
  val regRq = RegRq("a77x771@gmail.com", "alexr1", "r", "123-45-67", "M", "iOS13", "121212", "A", "B", "abc123DEF")
  /** Serialization: Case Class => String */
  val json: String = upickle.default.write(regRq)
  println(urlAuthReg)
  println(regRq)
  println(json)

  pprint.log(json)
  
  val r: Response = requests.post(urlAuthReg, headers = Seq(
    "Content-Type"->"application/json",
//    "Accept"->"*/*",
//    "Accept-Encoding"->"gzip, deflate, br"
  ), data = json)
  pprint.log(r)
  
}
