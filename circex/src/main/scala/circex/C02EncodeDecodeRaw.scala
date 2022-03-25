package circex

import io.circe.DecodingFailure
import io.circe.Json
import io.circe.syntax._

object C02EncodeDecodeRaw extends App {

  /** encoding */
  val intsJson: Json = List(1, 2, 3).asJson
  println(intsJson.noSpaces) // [1,2,3]

  /** decoding */
  val r: Either[DecodingFailure, List[Int]] = intsJson.as[List[Int]]
  pprint.pprintln(r) // Right(value = List(1, 2, 3))

}
