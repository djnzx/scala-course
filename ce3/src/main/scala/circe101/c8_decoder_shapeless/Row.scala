package circe101.c8_decoder_shapeless

import io.circe.Decoder
import io.circe.HCursor

case class Row(x: String, n1: Double, n2: Double, n3: Double, n4: Double, n5: Double, n6: Double)

object Row {

  implicit val rowDecoder: Decoder[Row] =
    (hc: HCursor) =>
      hc.as[(String, Double, Double, Double, Double, Double, Double)]
        .map((Row.apply _).tupled)

}
