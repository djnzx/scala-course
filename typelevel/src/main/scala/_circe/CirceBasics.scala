package _circe

import io.circe
import io.circe.Decoder.Result
import io.circe._                  /** Json */
import io.circe.syntax._           /** asJson */
import io.circe.generic.semiauto._ /** deriveEncoder */
import io.circe.parser._           /** decode */

/**
  * https://circe.github.io/circe/
  * https://circe.github.io/circe/codec.html
  */
object CirceBasics extends App {

  val intsJson: Json = List(1, 2, 3).asJson

  val r: Result[List[Int]] = intsJson.as[List[Int]]

  val decoded1: Either[circe.Error, List[Int]] = decode[List[Int]]("[1, 2, 3]")
  println(decoded1) // Right(List(1, 2, 3))

  val decoded2: Either[circe.Error, List[Int]] = decode[List[Int]]("[1, 2, 3")
  println(decoded2) // Left(io.circe.ParsingFailure: exhausted input)

  case class Box(a: Int, b: String)
  object Box {
    implicit val encoder: Encoder[Box] = deriveEncoder
    implicit val decoder: Decoder[Box] = deriveDecoder
  }

  val box = Box(1, "Alex")
  println(box.asJson.noSpaces)

  val json = """{"a":1,"b":"Alex"}"""
  val box2: Either[Error, Box] = decode[Box](json)
  box2.fold(
    e => println(s"E:$e"),
    box => println(s"BOX:$box")
  )
  // TODO: handle absence of fields as Option[A]
  // TODO: handle enum
  // TODO: handle sealed traits
  // TODO: custom logic
}
