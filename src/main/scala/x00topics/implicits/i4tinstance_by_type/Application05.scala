package x00topics.implicits.i4tinstance_by_type

import com.typesafe.config.{Config, ConfigFactory}
import scala.util.Try

object Application05 extends App {

  // generalize reader with type A
  trait XReader[A] {
    def read(config: Config, path: String): Either[Throwable, A]
  }

  // put implicit implementations into object
  object XReader {
    def apply[A](f: (Config, String) => A): XReader[A] = new XReader[A]() {
      override def read(config: Config, path: String): Either[Throwable, A] = Try(f(config, path)).toEither
    }
    // the main idea - hide implicits into object
    implicit val _int: XReader[Int]    = XReader[Int]((config: Config, path: String) => config.getInt(path))
    implicit val _str: XReader[String] = XReader[String]((config: Config, path: String) => config.getString(path))
  }

  // make class implicit to add the new behavior to the existent class
  implicit class SmartConfig(config: Config) {
    def as[A](path: String)(implicit reader: XReader[A]): Either[Throwable, A] = reader.read(config, path)
    def as1[A : XReader](path: String): Either[Throwable, A] = implicitly[XReader[A]].read(config, path)
  }

  // creating instance
  val conf: Config = ConfigFactory.load()

  // reading variables
  val v1: Either[Throwable, String] = conf.as[String]("simple-string")
  val v2: Either[Throwable, Int] =    conf.as[Int]("simple-int")
  val s: String = v1.fold( _ => "No var", s => s)
  val i: Int    = v2.fold( _ => -42, i => i)

  println(s)
  println(i)
}
