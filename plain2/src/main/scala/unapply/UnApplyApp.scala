package unapply

object UnApplyApp extends App {
  import scala.concurrent.duration._
  case class Config(windowSeconds: Int)
  val config = Config(windowSeconds = 3600)

  def represent(c: Config): String = c.windowSeconds.seconds.toCoarsest match {
    case Duration(1, HOURS) => "hourly"
    case Duration(2, HOURS) => "every two hours"
    case Duration(3, HOURS) => "every three hours"
    case Duration(4, HOURS) => "every four hours"
    case Duration(6, HOURS) => "every six hours"
    case Duration(8, HOURS) => "every eight hours"
    case Duration(1, DAYS)  => "daily"
    case Duration(7, DAYS)  => "weekly"
    case _                  => "invalid"

  }

  println(represent(Config(2 * 60 * 60))) // "every two hours"
  println(represent(Config(24 * 60 * 60))) // "daily"
}
