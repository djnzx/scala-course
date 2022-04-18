package fss.d5http4stream

import cats.effect._
import fs2.Stream
import fs2.text
import io.circe.parser.decode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.http4s._
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax
import scala.concurrent.ExecutionContext

object ConsumeOneByOne3AsyncFetchAsyncPrint extends IOApp.Simple {
  val rq = Request[IO](uri = uri"http://localhost:8080/s")

  /** acquiring thread pool */
  def tpAcquire(nThreads: Int): IO[ExecutorService] = IO(Executors.newFixedThreadPool(nThreads))

  /** releasing thread pool */
  def tpRelease(pool: ExecutorService): IO[Unit] = IO(pool.shutdown())

  /** ExecutionContext as a resource */
  def mkEcResource(nThreads: Int): Resource[IO, ExecutionContext] =
    Resource
      .make(tpAcquire(nThreads))(tpRelease)
      .map(ExecutionContext.fromExecutorService)

  /** stream of one element = ExecutionContext */
  def mkBlockingEcStream(nThreads: Int): Stream[IO, ExecutionContext] =
    Stream.resource(mkEcResource(nThreads))

  /** stream of one element = HttpClient, will run on the given ExecutionContext */
  def mkHttpClient(ec: ExecutionContext): Stream[IO, Client[IO]] = BlazeClientBuilder[IO]
    .withExecutionContext(ec)
    .stream

  // TODO: make printing to the console in the separate thread pool
  def oneByOne =
    mkBlockingEcStream(8)
      .flatMap { ec =>
        mkHttpClient(ec)
          .flatMap(_.stream(rq))
          .map(_.body)
          .flatMap(_.through(text.utf8.decode))
          .flatMap(raw => decode[Data](raw).fold(_ => Stream.empty, Stream.emit))
          .evalTap { d =>
            IO(println(s"${Thread.currentThread().getName}: $d"))
              .evalOn(ec)
          }
      }
      .compile
      .drain

  override def run = oneByOne
}
