package fss.d5http4stream

import cats.effect._
import fs2.Stream
import fs2.text
import io.circe.parser.decode
import java.util.concurrent.Executors
import org.http4s._
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax
import scala.concurrent.ExecutionContext

object ConsumeOneByOne2ASyncFetchSyncPrint extends IOApp.Simple {
  val rq = Request[IO](uri = uri"http://localhost:8080/s")

  // stream of one element = ExecutionContext
  def mkBlockingEcStream(nThreads: Int): Stream[IO, ExecutionContext] = Stream
    .bracket(IO(Executors.newFixedThreadPool(nThreads)))(pool => IO(pool.shutdown()))
    .map(ExecutionContext.fromExecutorService)

  // stream of one element = HttpClient
  // HttpClient will be run on the given ExecutionContext
  def mkHttpClient(ec: ExecutionContext): Stream[IO, Client[IO]] = BlazeClientBuilder[IO]
    .withExecutionContext(ec)
    .stream

  // TODO: make printing to the console in the separate thread pool
  def oneByOne =
    mkBlockingEcStream(8)
      .flatMap(mkHttpClient)
      .flatMap(_.stream(rq))
      .map(_.body)
      .flatMap(_.through(text.utf8.decode))
      .flatMap(raw => decode[Data](raw).fold(_ => Stream.empty, Stream.emit))
      .evalTap(d => IO(println(d)))
      .compile
      .drain

  override def run = oneByOne
}
