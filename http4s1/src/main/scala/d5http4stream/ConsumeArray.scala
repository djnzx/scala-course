package d5http4stream

import cats.effect._
import fs2.Stream
import io.circe.Json
import org.http4s._
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.implicits.http4sLiteralsSyntax
import org.typelevel.jawn.Facade
import org.typelevel.jawn.fs2.JsonStreamSyntax

object ConsumeArray extends IOApp.Simple {
  val rq = Request[IO](uri = uri"http://localhost:8080/s2")

  implicit val f: Facade[Json] = io.circe.jawn.CirceSupportParser.facade
  def waitForArray = BlazeClientBuilder[IO]
    .stream
    .flatMap(_.stream(rq))
    .flatMap(_.body.chunks.parseJsonStream)
    .flatMap(_.as[Seq[Data]].fold(_ => Stream.empty, Stream.emit))
    .evalTap(d => IO(println(d)))
    .compile
    .drain

  override def run = waitForArray
}
