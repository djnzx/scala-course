package x098

import com.typesafe.config.Config
import scala.util.Try

trait XReader[A] {
  def read(config: Config, path: String): Either[Throwable, A]
}

object XReader {

  def apply[A](read: (Config, String) => A): XReader[A] = (config: Config, path: String) =>
    Try(read(config, path)).toEither

  implicit val int_r  = XReader[Int]((config: Config, path: String) => config.getInt(path))
  implicit val str_r  = XReader[String]((config: Config, path: String) => config.getString(path))
  implicit val bool_r = XReader[Boolean]((config: Config, path: String) => config.getBoolean(path))
}


