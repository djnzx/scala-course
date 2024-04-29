package circex.trt

import io.circe.generic.extras.auto._
import io.circe.generic.extras.Configuration
import io.circe.syntax.EncoderOps
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SealedTraitWithDiscriminator extends AnyFunSuite with Matchers {

  test("1") {
    sealed trait T
    case class C1(x: String) extends T
    case class C2(y: Int) extends T

    implicit val circeConfig: Configuration = Configuration.default.withDiscriminator("typ")

    val c1: T = C1("A")
    val c2: T = C2(3)

    val j1 = c1.asJson
    val j2 = c2.asJson

    pprint.log(c1.asJson)
    pprint.log(c2.asJson)

    j1.noSpaces shouldBe
    """{"x":"A","typ":"C1"}"""

    j2.noSpaces shouldBe
    """{"y":3,"typ":"C2"}"""

  }

}
