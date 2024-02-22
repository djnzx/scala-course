package stackoverflowquestions

import stackoverflowquestions.ParseAccumulating.OptionOne
import stackoverflowquestions.ParseAccumulating.OptionTwo
import cats.implicits._
import io.circe._
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec

object ParseAccumulating {

  sealed trait Example

  case class OptionOne(event: String, time: String, platform: String) extends Example
  object OptionOne extends AutoDerivation

  case class OptionTwo(event: String, time: String) extends Example
  object OptionTwo extends AutoDerivation

  object Example {

    implicit val decoder: Decoder[Example] = List[Decoder[Example]](
      OptionOne.exportDecoder[OptionOne].instance.widen,
      OptionTwo.exportDecoder[OptionTwo].instance.widen
    ).reduce(_ or _)

  }

}

class ParseAccumulatingSpec extends AnyFunSpec {
  it("Decoder -- Example (invalid, multiple)") {

    val invalid = Json.obj(
      ("event", "randomEvent".asJson),
      ("time", 8.asJson),
      ("platform", 5.asJson)
    )

    // 2 errors
    println(OptionOne.exportDecoder[OptionOne].instance.decodeAccumulating(invalid.hcursor))
    // 1 error
    println(OptionTwo.exportDecoder[OptionTwo].instance.decodeAccumulating(invalid.hcursor))
    // 2 errors
//    println(accumulating[OptionOne](invalid.hcursor))
    // 1 error
//    println(accumulating[Example](invalid.hcursor))
    // 1
    println(Decoder[OptionOne].decodeJson(invalid))
    // 2
    println(Decoder[OptionOne].decodeAccumulating(invalid.hcursor))
  }
}
