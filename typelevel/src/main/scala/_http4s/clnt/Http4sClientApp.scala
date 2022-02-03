package _http4s.clnt

import cats.effect.IO
import cats.effect.IOApp
import org.http4s.Request
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.implicits.http4sLiteralsSyntax

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Http4sClientApp extends IOApp.Simple {

  val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))
  val rq = Request[IO](uri = uri"https://www.google.com")

  override def run: IO[Unit] = BlazeClientBuilder[IO](ec)
    .resource
    .use { client =>
      for {
        r <- client.expect[String](rq)
        _ <- IO { println(r) }
      } yield ()
    }

}
