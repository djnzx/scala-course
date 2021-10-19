package d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource

object A1_Idea extends IOApp {

  /** the idea is to wire acquire and release functions together
    * and call them automatically during unsafeRun
    * before and after .use()
    */
  def make[A](acquire: IO[A])(release: A => IO[Unit]): Resource[IO, A] = ???

  override def run(args: List[String]): IO[ExitCode] = ???
}
