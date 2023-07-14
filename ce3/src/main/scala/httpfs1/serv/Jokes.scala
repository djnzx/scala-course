package httpfs1.serv

import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto.deriveEncoder
import cats.effect._
import cats.implicits._
import io.circe._
import org.http4s.Method._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.client.Client
import org.http4s.client.dsl._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder}


trait Jokes[F[_]] {
  def get: F[Jokes.Joke]
}

object Jokes {

  final case class Joke(joke: String)
  object Joke {
    implicit val enc: Encoder[Joke] = deriveEncoder
    implicit val dec: Decoder[Joke] = deriveDecoder
    implicit def ee[F[_]]: EntityEncoder[F, Joke] = jsonEncoderOf
    implicit def de[F[_]: Concurrent]: EntityDecoder[F, Joke] = jsonOf
  }
  final case class JokeError(e: Throwable) extends RuntimeException

  def impl[F[_]: MonadCancelThrow: Concurrent](httpC: Client[F]): Jokes[F] = new Jokes[F] with Http4sClientDsl[F] {
    def get: F[Joke] = {
      val joke: F[Joke] = httpC
        .expect[Joke](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError { case t: Throwable => JokeError(t) }
      joke
    }
  }
}
