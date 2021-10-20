package ce2.d06_async

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp

object A1_Idea extends IOApp {

  type PlainCallback[A] = Either[Throwable, A] => Unit

  type Callback[A] = PlainCallback[A] => Unit

  def async[A](k: Callback[A]): IO[A] = ???

  override def run(args: List[String]): IO[ExitCode] = ???

}
