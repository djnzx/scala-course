package rtj.auth.adt

import io.circe.Encoder
import io.circe.Json
import io.circe.JsonObject
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SerializationWays extends AnyFunSpec with Matchers {

  describe("serialization ways") {

    case class Person(name: String, age: Int)
    val jim: Person = Person("jim", 33)
    val serialized: String = """{"name":"jim","age":33}"""

    it("way 1 - fully automatic, just add import `io.circe.generic.auto._`") {
      import io.circe.generic.auto._

      jim.asJson.noSpaces shouldEqual serialized
    }

    it("way 2 - almost automatic, just `extends AutoDerivation` trait") {
      import io.circe.generic.AutoDerivation

      object PersonDerivedAutomatically extends AutoDerivation
      import PersonDerivedAutomatically._

      jim.asJson.noSpaces shouldEqual serialized
    }

    it("way 3 - explicitly automatic, just call `deriveEncoder`") {
      import io.circe.generic.semiauto.deriveEncoder

      object PersonDerivedManually {
        implicit val encoder = deriveEncoder[Person]
      }
      import PersonDerivedManually._

      jim.asJson.noSpaces shouldEqual serialized
    }

    it("way 4 - fully manual") {
      object PersonManual {
        implicit val encoder: Encoder[Person] = Encoder.apply { p =>
          JsonObject(
            "name" -> p.name.asJson,
            "age"  -> p.age.asJson,
          ).asJson
        }
      }
      import PersonManual._

      jim.asJson.noSpaces shouldEqual serialized
    }

    it("way 5 - automatic, but altering whatever we need") {
      import io.circe.generic.semiauto.deriveEncoder

      object PersonDerivedManually2 {
        implicit val encoder: Encoder[Person] = Encoder { p =>
          val fieldsDerived: Encoder[Person] = deriveEncoder
          val fieldsContextBased: Encoder[Person] = Encoder { p =>
            Map("nameUpper" -> p.name.toUpperCase.asJson).asJson
          }
          val fieldsWoContext: Encoder[Person] = Encoder { _ => Map("extraField" -> true.asJson).asJson }

          List(fieldsDerived, fieldsContextBased, fieldsWoContext)
            .map(_(p))
            .reduce(_ deepMerge _)
        }
      }
      import PersonDerivedManually2._

      val serialized: Json = jim.asJson

      val expected: Json = JsonObject(
        "name"       -> "jim".asJson,
        "age"        -> 33.asJson,
        "nameUpper"  -> "JIM".asJson,
        "extraField" -> true.asJson,
      ).asJson

      serialized shouldEqual expected
    }

  }

}
