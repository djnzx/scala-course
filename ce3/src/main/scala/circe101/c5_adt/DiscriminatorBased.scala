package circe101.c5_adt

import circe101.Base
import io.circe.Json
import io.circe.generic.extras.Configuration
import io.circe.syntax.EncoderOps

// https://circe.github.io/circe/codecs/adt.html
class DiscriminatorBased extends Base {

  object domain {
    sealed trait T
    case class C(a: Int) extends T
  }

  test("concrete type makes difference!!!") {
    import io.circe.generic.extras.auto._
    implicit val config: Configuration = Configuration.default.withDiscriminator("type")

    import domain._

    val x: C = C(3) // object[B -> 3              ]
    val y: T = C(3) // object[B -> 3, type -> "C1"]

    pprint.log(x.asJson) // NO  `type`, since `C1` is a case class
    pprint.log(y.asJson) // HAS `type`, since `TT` is a sealed hierarchy

    x.asJson shouldBe Json.obj(
      "a" -> Json.fromInt(3)
    )

    y.asJson shouldBe Json.obj(
      "a"    -> Json.fromInt(3),
      "type" -> Json.fromString("C"),
    )
  }

}
