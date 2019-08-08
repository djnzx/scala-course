package _implicits.x0

import com.typesafe.config.Config

import scala.util.Try

trait XReader[A] {
  def read(config: Config, path: String): Either[Throwable, A]
}

object XReader {
  type Result[A] = Either[Throwable, A]
  def apply[A](f: (Config, String) => A): XReader[A] = new XReader[A]() {
    override def read(config: Config, path: String): Either[Throwable, A] = Try(f(config, path)).toEither
  }

  implicit val _int: XReader[Int]    = XReader[Int]((config: Config, path: String) => config.getInt(path))
  implicit val _str: XReader[String] = XReader[String]((config: Config, path: String) => config.getString(path))
}
