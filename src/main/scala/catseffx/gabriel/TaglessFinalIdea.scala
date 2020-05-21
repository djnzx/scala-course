package catseffx.gabriel

import cats.{Applicative, Monad}
import cats.effect.concurrent.Ref
import cats.effect.{Async, ExitCode, IO, IOApp, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.applicative._

import scala.collection.SortedSet
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object TaglessFinalIdea extends IOApp {

  // algebra
  trait Console[F[_]] {
    def putStrLn(s: String): F[Unit]
    def readLn: F[String]
  }

  // different implementations
  class StdConsole[F[_]: Sync] extends Console[F] {
    override def putStrLn(s: String): F[Unit] = Sync[F].delay(println(s))
    override def readLn: F[String] = Sync[F].delay(scala.io.StdIn.readLine())
  }
  class RemoteConsole[F[_]: Async] extends Console[F] {
    private def fromFuture[A](fa: F[Future[A]]): F[A] =
      fa.flatMap { future =>
        Async[F].async { cb =>
          future.onComplete {
            case Success(v) => cb(Right(v))
            case Failure(ex) => cb(Left(ex))
          }
        }
      }

    override def putStrLn(s: String): F[Unit] = fromFuture(
      ???
      //Sync[F].delay(HttpClient.post(s))
    )
    override def readLn: F[String] = fromFuture(
      ???
      //Sync[F].delay(HttpClient.get)
    )
  }
  class TestConsole[F[_]: Applicative](state: Ref[F, List[String]]) extends Console[F] {
    override def putStrLn(s: String): F[Unit] = state.update(_ :+ s)
    override def readLn: F[String] = "test".pure[F]
  }

  // our application
  def app[F[_]: Monad](implicit C: Console[F]): F[Unit] = for {
    _ <- C.putStrLn("Enter your name:")
    s <- C.readLn
    _ <- C.putStrLn(s"Hello, $s!")
  } yield ()

  // test
  val test = {
    val spec = for {
      state <- Ref.of[IO, List[String]](List.empty)
      // betterMonadicFor syntax for implicit inside for
      implicit0(c: Console[IO]) = new TestConsole[IO](state)
      _     <- app[IO]
      st    <- state.get
      as    <- IO { assert(st == List("Enter your name:", "Hello, test!"))}
    } yield as

    spec.unsafeToFuture()
  }

  implicit val c: Console[IO] = new StdConsole
  // running
  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- app[IO]
  } yield ExitCode.Success
}
