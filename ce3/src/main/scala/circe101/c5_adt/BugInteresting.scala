package circe101.c5_adt

import circe101.Base
import io.circe.Json
import io.circe.generic.extras.Configuration
import io.circe.syntax.EncoderOps

class BugInteresting extends Base {

  test("scalatest/circe/pprint BUG !!!") {
    sealed trait T
    object T {
      case class C(x: Int) extends T
    }
    import io.circe.generic.auto._

    val c1: T = T.C(1)
    val j = c1.asJson

//    pprint.log(c1.asJson) // DOESN'T compile

    pprint.log(j) // GOOD !!!
  }

  test("scalatest/circe BUG!!! - doesn't compile") {
    sealed trait T
    object T {
      case class C(x: Int) extends T
    }

    implicit val config: Configuration = Configuration.default
      .withScreamingSnakeCaseMemberNames // works
      .withDiscriminator("type")         // doesn't
    import io.circe.generic.extras.auto._

    val c1: T = T.C(1)
    val j = c1.asJson

//    c1.asJson shouldBe Json.obj( // DOESN'T compile
//      "X"    -> Json.fromInt(1),
//      "type" -> Json.fromString("C"),
//    )

    j shouldBe Json.obj(
      "X"    -> Json.fromInt(1),
      "type" -> Json.fromString("C"),
    )
  }

}
