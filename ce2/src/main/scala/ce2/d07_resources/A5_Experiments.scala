package ce2.d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.implicits._

object A5_Experiments extends IOApp {

  val ra: Resource[IO, Int] = Resource.make(IO(println("Acquire A")) >> IO(1))(_ => IO(println("Release A")))
  val rb: Resource[IO, String] = Resource.make(IO(println("Acquire B")) >> IO("A"))(_ => IO(println("Release B")))

  val rabt: Resource[IO, (Int, Int)] = (ra, ra).tupled

  val rab = ra.flatMap(_ => rb)

  override def run(args: List[String]): IO[ExitCode] =
    rab
      .use(_ => IO(println("inside")))
      .as(ExitCode.Success)
}
