package circex

import io.circe.Decoder
import io.circe.Encoder
import io.circe.jawn.decode
import io.circe.syntax._
import io.circe.shapes._
import shapeless.Coproduct
import shapeless.Generic
import io.circe.generic.auto._

/** having "circe-shapes"
  *   - Event can be generalized
  */
object C12Adt2Shapeless extends App {
  import ModelAdt._

  implicit def encodeAdt[X <: Coproduct](implicit g: Generic.Aux[Event, X], ex: Encoder[X]): Encoder[Event] =
    ex.contramap(g.to)

  implicit def decodeAdt[X <: Coproduct](implicit g: Generic.Aux[Event, X], dx: Decoder[X]): Decoder[Event] =
    dx.map(g.from)

  println(Foo(33).asJson.noSpaces) // {"i":33}

  val raw =
    """
      |{"i":33}
      |""".stripMargin

  pprint.pprintln(decode[Event](raw)) // Right(value = Foo(i = 33))

}
