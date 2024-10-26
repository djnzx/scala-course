object LookForSbtCredentials extends App {

  private def readFile(path: String): String = {
    val source = scala.io.Source.fromFile(path)
    try source.mkString
    finally source.close()
  }

  val credentialsPath = System.getenv().getOrDefault("SBT_CREDENTIALS", s"${System.getProperty("user.home")}/.ivy2/.credentials")
  val text            = readFile(credentialsPath)

  val settings = text
    .split("\n")
    .map(_.split("="))
    .flatMap {
      case Array(a, b) => Some(a -> b)
      case _           => None
    }
    .toMap

  println(settings)

}
