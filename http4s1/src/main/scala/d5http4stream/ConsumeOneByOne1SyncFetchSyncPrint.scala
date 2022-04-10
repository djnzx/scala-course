package d5http4stream

import cats.effect._
import fs2.Stream
import fs2.text
import io.circe.parser.decode
import org.http4s._
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.implicits.http4sLiteralsSyntax

object ConsumeOneByOne1SyncFetchSyncPrint extends IOApp.Simple {
  val rq = Request[IO](uri = uri"http://localhost:8080/s")

  def oneByOne = BlazeClientBuilder[IO]
    .stream
    .flatMap(_.stream(rq))
    .map(_.body)
    .flatMap(_.through(text.utf8.decode))
    .flatMap(raw => decode[Data](raw).fold(_ => Stream.empty, Stream.emit))
    .evalTap(d => IO(println(d)))
    .compile
    .drain

  override def run = oneByOne
}
