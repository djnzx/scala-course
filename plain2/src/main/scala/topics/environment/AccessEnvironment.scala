package topics.environment

object AccessEnvironment {
  val K_NAME = "AMT_USER"
  val K_PASSWD = "AMT_PASSWD"

  case class Credentials(user: String, password: String)

  val user:   Option[String] = Option(System.getenv(K_NAME))
  val passwd: Option[String] = Option(System.getenv(K_PASSWD))
  val c1: Option[Credentials] = user.flatMap(u => passwd.map(p => Credentials(u, p)))
  
  // or
  val c2: Option[Credentials] = sys.env match {
    case kvs => kvs.get(K_NAME).flatMap(u => kvs.get(K_PASSWD).map(p => Credentials(u, p))) 
  }
  
  // or
  val c3: Option[Credentials] = sys.env match {
    case kvs => for {
      u <- kvs.get(K_NAME)
      p <- kvs.get(K_PASSWD)
    } yield Credentials(u, p)
  }

}
