package httpfs1.serv

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.Method._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._
import org.http4s.{EntityDecoder, EntityEncoder}

trait Jokes[F[_]]{
  def get: F[Jokes.Joke]
}

object Jokes {
  def apply[F[_]](implicit ev: Jokes[F]): Jokes[F] = ev

  final case class Joke(joke: String) extends AnyVal
  object Joke {
    // decoder to decode request from another server
    implicit val jokeDecoder: Decoder[Joke] = deriveDecoder[Joke]
    implicit def jokeEntityDecoder[F[_]: Sync]: EntityDecoder[F, Joke] = jsonOf
    // encoder to encode our response
    implicit val jokeEncoder: Encoder[Joke] = deriveEncoder[Joke]
    implicit def jokeEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Joke] = jsonEncoderOf
  }

  final case class JokeError(e: Throwable) extends RuntimeException

  def impl[F[_]: Sync](httpC: Client[F]): Jokes[F] = new Jokes[F]{
    val dsl = new Http4sClientDsl[F]{}
    import dsl._
    def get: F[Jokes.Joke] = {
      val joke: F[Joke] = httpC.expect[Joke](GET(uri"https://icanhazdadjoke.com/"))
        // handle specific type of exception by partial function
        .adaptError { e: Throwable => e match {
          case t: Throwable => JokeError(t)
        }}

      joke
    }
  }
}
