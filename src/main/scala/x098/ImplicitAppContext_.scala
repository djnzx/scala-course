package x098

import _implicits.x0.XReader
import com.typesafe.config.{Config, ConfigFactory}

object ImplicitAppContext_ extends App {

  implicit class SmartConfig(config: Config) {
    def as[A : XReader](path: String): Either[Throwable, A] = implicitly[XReader[A]].read(config, path)
  }

  val conf: Config = ConfigFactory.load()

  val foo: Either[Throwable, String] = conf.as[String]("simple-string")
  val bar: Either[Throwable, Int] =    conf.as[Int]("simple-int")
  val s: String = foo.fold( _ => "-13", s => s)
  val i: Int    = bar.fold( _ => -42, i => i)

  println(foo)
  println(bar)

  println(s)
  println(i)

}
