package circex

import cats.syntax.functor._
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.auto._
import io.circe.jawn.decode
import io.circe.syntax._

object ModelAdt {
  sealed trait Event
  case class Foo(i: Int) extends Event
  case class Bar(s: String) extends Event
}

object C11Adt1Plain extends App {
  import ModelAdt._

  /** manual encoder */
  implicit val e: Encoder[Event] = Encoder.instance {
    case foo: Foo => foo.asJson
    case bar: Bar => bar.asJson
  }

  /** sequential decoder */
  implicit val d: Decoder[Event] =
    List[Decoder[Event]](
      Decoder[Foo].widen,
      Decoder[Bar].widen,
    ).reduceLeft(_ or _)

  println(Foo(33).asJson.noSpaces) // {"i":33}

  val raw =
    """
      |{"i":33}
      |""".stripMargin

  pprint.pprintln(decode[Event](raw)) // Right(value = Foo(i = 33))

}
