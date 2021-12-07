package httclient

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import org.http4s.Method
import org.http4s.Request
import org.http4s.Status
import org.http4s.Uri
import org.http4s.client.Client
import org.http4s.client.jdkhttpclient.JdkHttpClient
import org.http4s.implicits._

object Intro extends IOApp {

  def putStrLn(s: String): IO[Unit] =
    IO(println(s))

  def fetchStatus[F[_]](c: Client[F], uri: Uri): F[Status] =
    c.status(Request[F](Method.GET, uri = uri))

  val client: IO[Client[IO]] = JdkHttpClient.simple[IO]

  val result: IO[Either[Throwable, Status]] = client.flatMap { c =>
    fetchStatus(c, uri"https://http4s.org/").attempt
  }

  override def run(args: List[String]): IO[ExitCode] =
    result
      .map(_.toString)
      .flatMap { s => putStrLn(s) }
      .as(ExitCode.Success)

}
