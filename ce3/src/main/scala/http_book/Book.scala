package http_book

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class Book(name: String, author: String)
object Book {
  implicit val c: Codec[Book] = deriveCodec
}
