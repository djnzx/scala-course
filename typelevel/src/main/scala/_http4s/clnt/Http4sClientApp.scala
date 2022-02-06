package _http4s.clnt

import cats.effect.IO
import cats.effect.IOApp
import org.http4s.Request
import org.http4s.Uri
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.implicits.http4sLiteralsSyntax

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Http4sClientApp extends IOApp.Simple {

  val ec: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))

  override def run: IO[Unit] = BlazeClientBuilder[IO](ec)
    .resource
    .use { client =>
      for {
        r <- client.expect[String](Uri.uri("https://news.ycombinator.com/"))
        _ <- IO { println(r) }
      } yield ()
    }

}
