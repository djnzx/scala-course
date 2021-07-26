package _circe

import io.circe._                  /** Json */
import java.util.UUID
import io.circe.syntax._           /** asJson */
import io.circe.generic.semiauto._ /** deriveEncoder */

/**
  * https://circe.github.io/circe/
  * https://circe.github.io/circe/codec.html
  */
object CirceBasics2 extends App {

  case class PersonId(id: UUID)
  object PersonId {
    def next = PersonId(UUID.randomUUID())
    implicit val encoder: Encoder[PersonId] = deriveEncoder
    implicit val decoder: Decoder[PersonId] = deriveDecoder
  }
  val pid = PersonId.next

  val s1 = pid.asJson.noSpaces
  println(s1)

  val s2 = pid.toString
  println(s2)
}
