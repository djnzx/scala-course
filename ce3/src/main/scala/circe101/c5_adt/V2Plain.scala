package circe101.c5_adt

import cats.syntax.functor._
import circe101.Base
import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

class V2Plain extends Base {

  import Model._
  import Event._

  test("manual explicit encoder") {
    implicit val e: Encoder[Event] = Encoder.instance {
      case foo: Foo => foo.asJson // import io.circe.generic.auto._
      case bar: Bar => bar.asJson // import io.circe.generic.auto._
    }

    Event.Foo(33).asJson.noSpaces shouldBe
    """
        |{"i":33}
        |""".stripMargin.trim
  }

  test("sequential encoder") {
    implicit val e: Encoder[Event] = Encoder.instance {
      case foo: Foo => Encoder[Foo].apply(foo) // import io.circe.generic.auto._
      case bar: Bar => Encoder[Bar].apply(bar) // import io.circe.generic.auto._
    }

    Event.Foo(33).asJson.noSpaces shouldBe
      """
        |{"i":33}
        |""".stripMargin.trim
  }

  test("sequential decoder") {
    implicit val d: Decoder[Event] =
      List[Decoder[Event]](
        Decoder[Foo].widen, // import io.circe.generic.auto._
        Decoder[Bar].widen, // import io.circe.generic.auto._
      ).reduceLeft(_ or _)

    val raw =
      """
        |{"i":33}
        |""".stripMargin

    inside(decode[Event](raw)) {
      case Right(x) => x shouldEqual Foo(33)
    }
  }

}
