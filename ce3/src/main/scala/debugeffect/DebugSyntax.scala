package debugeffect

import cats._
import cats.effect._
import cats.effect.std.Console
import cats.implicits._
import fs2._

// TODO:
//  1. introduce defer
//  2. design debug state
//  -. terminate app
//  -. continue
//  -. continue w/o pauses
//  -. skip N steps
object DebugSyntax {

  case class TraceState(skip: Option[Int], terminate: Boolean, continue: Boolean)

  object TraceState {
    val initial = TraceState(None, false, false)
  }

  implicit class DebugOps[F[_]: Monad: Sync: Console, A](fa: F[A]) {
    def debugW(implicit ts: Ref[F, TraceState]): F[A] =
      fa.flatTap { a =>
        val colored = pprint.apply(a)
        F.println(colored) >>
          F.print(">") >>
          F.readLine
//            .flatMap {
//            case "t" => F.delay(System.exit(-1))
//            case _   => ().pure[F]
//          }
      }
  }

  implicit class DebugStreamOps[F[_]: Sync: Console, A](as: Stream[F, A]) {
    def debugW(implicit ts: Ref[F, TraceState]): Stream[F, A] =
      as.evalTap(a => F.delay(a).debugW)
  }

}

object TestApp extends IOApp.Simple {

  import DebugSyntax._

  val app =
//    Deferred[IO, Unit].flatMap{ shutdown =>
//      val app = buildMyAppResource(shutdown)
//      IO.race(shutdown.get, app)
//    }
    Ref[IO]
      .of(TraceState.initial)
      .flatTap { implicit ts =>
        Stream
          .emits(1 to 10)
          .covary[IO]
          .debugW
          .compile
          .drain
      }
      .void

  override def run: IO[Unit] = app

}
