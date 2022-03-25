package _circe

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredDecoder
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object CirceFieldNaming extends App {

  object CirceConfigurations {
    object camel {
      implicit val c: Configuration = Configuration.default.withDefaults
    }
    object kebab {
      implicit val c: Configuration = Configuration.default.withKebabCaseMemberNames
    }
    object snake {
      implicit val c: Configuration = Configuration.default.withSnakeCaseMemberNames
    }

  }

  case class Details(userId: Int)
//  object Details {
//    implicit val e: Encoder[Details] = deriveEncoder
//  }
  object DetailsCamelCase {
//    implicit val c = CirceConfs.camel
    val e: Encoder[Details] = deriveConfiguredEncoder
    val d: Decoder[Details] = deriveConfiguredDecoder
  }
  object DetailsKebabCase {
//    implicit val c = CirceConfs.kebab
    val e: Encoder[Details] = deriveConfiguredEncoder
    val d: Decoder[Details] = deriveConfiguredDecoder
  }
  object DetailsSnakeCase {
//    implicit val c = CirceConfigurations.snake
    val e: Encoder[Details] = deriveConfiguredEncoder
    val d: Decoder[Details] = deriveConfiguredDecoder
  }

  val details = Details(33)

  // camelCase, by default
  print("camelCase: ")
  println(details.asJson(DetailsCamelCase.e).noSpaces)

  print("kebabCase: ")
  println(details.asJson(DetailsKebabCase.e).noSpaces)

  print("snakeCase: ")
  println(details.asJson(DetailsSnakeCase.e).noSpaces)

  println(io.circe.parser.decode[Details]("""{"userId":33}""")(DetailsCamelCase.d))
  println(io.circe.parser.decode[Details]("""{"user-id":33}""")(DetailsKebabCase.d))
  println(io.circe.parser.decode[Details]("""{"user_id":33}""")(DetailsSnakeCase.d))

}

class CirceFieldNamingSpec extends AnyFunSpec with Matchers {}
