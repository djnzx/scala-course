package http_book

import cats.effect.IO
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

case class Book(name: String, author: String)
object Book {
  // encoder - circe
  implicit val enc: Encoder[Book] = deriveEncoder[Book]
  // entity encoder - http4s
  implicit val ee: EntityEncoder[IO, Book] = jsonEncoderOf[IO, Book]

  // decoder - circe
  implicit val dec: Decoder[Book] = deriveDecoder[Book]
  // entity decoder - http4s
  implicit val ed: EntityDecoder[IO, Book] = jsonOf[IO, Book]
}
