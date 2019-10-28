package _implicits.x0

import com.typesafe.config.{Config, ConfigFactory}

object ImplicitAppContext extends App {

  implicit class SmartConfig(config: Config) {
    def as1[A : XReader](path: String): Either[Throwable, A] = implicitly[XReader[A]].read(config, path)
    def as2[A](path: String)(implicit reader: XReader[A]): XReader.Result[A] = reader.read(config, path)
  }

  val conf: Config = ConfigFactory.load()

  val foo: Either[Throwable, String] = conf.as1[String]("simple-string")
  val bar: Either[Throwable, Int] =    conf.as1[Int]("simple-int")
  val s: String = foo.fold( _ => "-13", s => s)
  val i: Int    = bar.fold( _ => -42, i => i)

  println(s)
  println(i)

//  println(foo)
//  println(bar)
}
