package httpfs1.serv

import cats.Applicative
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._

trait HelloWorld[F[_]]{
  def hello(n: HelloWorld.Name): F[HelloWorld.Greeting]
}

/**
  * service
  */
object HelloWorld {
  implicit def apply[F[_]](implicit ev: HelloWorld[F]): HelloWorld[F] = ev

  // service param(s)
  final case class Name(name: String) extends AnyVal
  // service response
  final case class Greeting(greeting: String) extends AnyVal
  // implicits to encode Greeting to JSON
  object Greeting {
    implicit val greetingEncoder: Encoder[Greeting] = new Encoder[Greeting] {
      final def apply(a: Greeting): Json = Json.obj(
        ("message", Json.fromString(a.greeting)),
      )
    }
    implicit def greetingEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Greeting] =
      jsonEncoderOf//[F, Greeting]
  }

  // real implementation
  def impl[F[_]: Applicative]: HelloWorld[F] = new HelloWorld[F]{
    def hello(n: HelloWorld.Name): F[HelloWorld.Greeting] = {
      // have a value
      val g = Greeting(s"Hello, ${n.name}")
      // lift to applicative
      g.pure[F]
    }
  }
}
