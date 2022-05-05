package _so

import _so.ParseAccumulating.OptionOne
import _so.ParseAccumulating.accumulating
import cats.implicits._
import io.circe._
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps
import org.scalatest.funspec.AnyFunSpec

object ParseAccumulating {

  sealed trait Example

  case class OptionOne(
      event: String,
      time: String,
      platform: String
  ) extends Example
  object OptionOne extends AutoDerivation

  case class OptionTwo(
      event: String,
      time: String
  ) extends Example
  object OptionTwo extends AutoDerivation

  def accumulating[A](hc: HCursor)(implicit da: Decoder[A]): Decoder.Result[A] =
    da.decodeAccumulating(hc)
      .toEither
      .leftMap { dff => DecodingFailure("errors", dff.toList.flatMap(_.history)) }

  implicit val decoder: Decoder[Example] = List[Decoder[Example]](
    hc => accumulating[OptionOne](hc),
    hc => accumulating[OptionTwo](hc)
  ).reduce(_ or _)

}

class ParseAccumulatingSpec extends AnyFunSpec {
  it("Decoder -- Example (invalid, multiple)") {

    val invalid = Json.obj(
      ("event", "randomEvent".asJson),
      ("time", 8.asJson),
      ("platform", 5.asJson)
    )

    println(accumulating[OptionOne](invalid.hcursor))
  }
}
