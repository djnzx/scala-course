package request

object Headers {
  
  val headers = Seq(
    "Content-Type"->"application/json",
  )

  def auth(token: String) = ("Authorization"->s"Bearer $token")

  def headersAuth(token: String) = headers :+ auth(token)

}
