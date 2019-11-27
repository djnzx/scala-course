package x00topics.environment

import scala.jdk.CollectionConverters._

object EnvApp extends App {
  def user:   Option[String] = Option(System.getenv("AMT_USER"))
  def passwd: Option[String] = Option(System.getenv("AMT_PASSWD"))
  case class Credentials(user: String, password: String)
  val c: Option[Credentials] = user.flatMap(u => passwd.map(p => Credentials(u, p)))
  println(c)

//  val properties = System.getProperties.asScala
//  for ((k,v) <- properties) println(s"key: $k, value: $v")
}
