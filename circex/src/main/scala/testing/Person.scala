package testing

import io.circe.Decoder
import io.circe.Encoder
import io.circe.syntax.EncoderOps

case class Person(name: String)
object Person {
  implicit val encPerson: Encoder[Person] = Encoder.forProduct1("name")(_.name)
  implicit val decPerson: Decoder[Person] = Decoder.forProduct1("name")(Person.apply)
}

object A extends App {
  val r = Person("James").asJson.as[Person]
  pprint.pprintln(r)
}
