package monaderror

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Sync
import cats.implicits._

object MEApp1 extends IOApp.Simple {

  type F[A] = IO[A]

  def make(x: Int) = x match {
    case 1 => Sync[F].raiseError(new IllegalArgumentException())
    case 2 => Sync[F].raiseError(new IllegalStateException())
    case n => n.toString.pure[F]
  }

  override def run: IO[Unit] = for {
    s <- make(2)
    _ <- IO.println(s)
  } yield ()

}
