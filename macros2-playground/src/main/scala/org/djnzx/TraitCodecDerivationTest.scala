package org.djnzx

import io.circe.Codec
import io.circe.syntax.EncoderOps
import org.djnzx.Playground.Info
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object Playground {

  trait Info {
    def name: String
    def level: Int
  }

  object Info {
    implicit val codec: Codec[Info] = TraitCodecMacros.deriveTraitCodec
  }

}

class TraitCodecDerivationTest extends AnyFunSuite with Inside with Matchers {

  val x1 = new Info {
    def name: String = "Scala"
    def level: Int = 3
  }

  val x2 = new Info {
    def name: String = "Java"
    def level: Int = 8
  }

  val rawJson =
    """
      |{"name":"Scala","level":3}
      |""".stripMargin

  test("1") {
    println(x1.asJson.noSpaces)
    println(x2.asJson.noSpaces)
  }

  test("2") {
    inside(io.circe.parser.decode[Info](rawJson)) {
      case Right(x) =>
        x.name shouldBe "Scala"
        x.level shouldBe 3
    }
  }

}
