package d01

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.unsafe.implicits.global

import scala.concurrent.Future

object App01 extends IOApp {

  val io1: IO[Nothing] = IO.raiseError(new IllegalArgumentException("intentionally"))
  val io2: IO[String] = io1.handleErrorWith(_ => IO("good"))
  val io3: IO[Either[Throwable, String]] = io2.attempt
//  io2.onError()
//  io2.redeem()
//  io2.redeemWith()
  val f1: Future[Either[Throwable, String]] = io3.unsafeToFuture()

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      r <- io2
      _ <- IO(println(r))
    } yield ExitCode.Success

  }

}
