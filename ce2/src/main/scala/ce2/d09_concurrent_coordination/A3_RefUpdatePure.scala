package ce2.d09_concurrent_coordination

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import ce2.common.debug.DebugHelper

object A3_RefUpdatePure extends IOApp {

  def task(id: Int, ref: Ref[IO, Int]): IO[Unit] =
    ref
      .modify(previous => id -> IO(s"$previous->$id").debug)
      .flatten
      .replicateA(3)
      .void

  def run(args: List[String]): IO[ExitCode] =
    for {
      ref <- Ref[IO].of(0)
      _ <- List(1, 2, 3).parTraverse(task(_, ref))
    } yield ExitCode.Success

}
