package circe101.c5_adt

import cats.implicits.catsSyntaxEitherId
import circe101.Base
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.auto._
import io.circe.jawn.decode
import io.circe.shapes._
import io.circe.syntax._
import shapeless.Coproduct
import shapeless.Generic

/** having "circe-shapes"
  *   - Event can be generalized
  */
class V3ShapelessBased extends Base {
  import Model._

  implicit def encodeAdt[X <: Coproduct](implicit g: Generic.Aux[Event, X], ex: Encoder[X]): Encoder[Event] =
    ex.contramap(g.to)

  implicit def decodeAdt[X <: Coproduct](implicit g: Generic.Aux[Event, X], dx: Decoder[X]): Decoder[Event] =
    dx.map(g.from)

  val data = Event.Foo(33)
  val json =
    """
      |{"i":33}
      |""".stripMargin.trim

  test("encode") {
    data.asJson.noSpaces shouldBe json
  }

  test("decode") {
    decode[Event](json) shouldBe data.asRight
  }
}
