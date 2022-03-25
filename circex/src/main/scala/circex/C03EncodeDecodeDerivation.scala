package circex

import io.circe
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.jawn.decode
import io.circe.syntax._

/** need to import "circe-generic" */
object C03EncodeDecodeDerivation extends App {

  case class Person(id: Int, name: String)
  implicit val e: Decoder[Person] = deriveDecoder
  implicit val d: Encoder[Person] = deriveEncoder

  val jim = Person(33, "Jim")
  pprint.pprintln(jim.asJson.noSpaces) // {"id":33,"name":"Jim"}

  val r: Either[circe.Error, Person] = decode[Person]("{\"id\":33,\"name\":\"Jim\"}")
  pprint.pprintln(r) // Right(value = Person(id = 33, name = "Jim"))
}
