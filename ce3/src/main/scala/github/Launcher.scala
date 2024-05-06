package github

import cats.effect.{IO, IOApp}
import cats.implicits.catsSyntaxEitherId
import org.http4s.Request
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder

object Launcher extends IOApp.Simple {

  val token = env.token.getOrElse(throw new IllegalStateException("no token"))

  def mkReq: Request[IO] = ???

  def fetch(client: Client[IO]): IO[Either[String, String]] =
    client
      .expect[String](mkReq)
      .map(_.asRight)
      .handleError(x => x.getMessage.asLeft)

  val clientR = EmberClientBuilder.default[IO].build

  val rsIO = clientR.use(fetch)

  override def run: IO[Unit] =
    rsIO
      .flatMap(x => IO(pprint.log(x)))

}
