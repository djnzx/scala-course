package circe101.c2_basics_decoding

import cats.data.Validated
import cats.implicits.catsSyntaxEitherId
import circe101.Base
import io.circe
import io.circe.Json
import io.circe.ParsingFailure
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps

class ParsingObjects extends Base {

  object domain {
    case class Person(name: String, age: Int)
    object Person extends AutoDerivation

    val p = Person("jim", 33)

    val raw = """{
                |  "name" : "jim",
                |  "age" : 33
                |}""".stripMargin
  }

  test("case classes - approach 1") {
    import domain._

    val parsed: Either[ParsingFailure, Json] = io.circe.parser.parse(raw)
    inside(parsed) {
      case Right(j) =>
        j shouldBe p.asJson
        j.as[Person] shouldBe p.asRight
    }
  }

  test("case classes - approach 2") {
    import domain._

    val parsed: Either[circe.Error, domain.Person] = io.circe.parser.decode[Person](raw)
    inside(parsed) {
      case Right(j) =>
        j shouldBe p
    }
  }

  test("case classes - errors handling") {
    import domain._

    val parsed: Either[circe.Error, domain.Person] = io.circe.parser.decode[Person]("{}")
    inside(parsed) {
      case Left(e) =>
        e.getStackTrace shouldBe "DecodingFailure at .name: Missing required field"
    }
  }

  test("case classes - errors handling, accumulating") {
    import domain._

    val parsed = io.circe.parser.decodeAccumulating[Person]("{}")
    inside(parsed) {
      case Validated.Invalid(nel) =>
        nel.toList match {
          case e1 :: e2 :: Nil =>
            e1.getMessage shouldBe "DecodingFailure at .name: Missing required field"
            e2.getMessage shouldBe "DecodingFailure at .age: Missing required field"
          case _               => sys.error("I don't care for now")
        }
    }
  }

}
