package vigil_test

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._

object Launcher extends IOApp {

  private def validateParams(args: List[String]): Either[String, (String, String)] = args match {
    case in :: out :: _ => (in, out).asRight
    case _              => "Source and Target paths expected as parameters".asLeft
  }

  override def run(args: List[String]): IO[ExitCode] =
    validateParams(args)
      .fold(
        errorMessage => IO.println(errorMessage).as(ExitCode.Error),
        { case (in, out) => Application.go[IO](in, out).as(ExitCode.Success) }
      )

}
