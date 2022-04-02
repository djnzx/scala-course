package course.d4delaypure

import cats.effect.std.Console
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Sync
import cats.implicits._

class Explore[F[_]: Sync: Console] {

  def printMe(x: String) = Console[F].println(x)

  def whatever = {

    // def pure[A](x: A): F[A]
    // def delay[A](x: => A): F[A]
    val x1: F[Int] = Sync[F].pure(5)
    val x2: F[Int] = Sync[F].delay(5 / 0)
    x2.attempt
      .flatMap {
        case Left(x)  => ???
        case Right(a) => ???
      }
    ().pure[F]
  }

}

object DelayPureApp extends IOApp.Simple {

  override def run: IO[Unit] = new Explore[IO].whatever
}
