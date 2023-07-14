package circex

import io.circe.Decoder
import io.circe.Encoder
import io.circe.jawn.decode
import io.circe.syntax.EncoderOps

import java.time.Instant
import scala.util.Try

/** map, emap, emapTry, contramap */
object C09ReusingEncodersDecoders extends App {

  implicit val encodeInstant: Encoder[Instant] =
    Encoder.encodeString.contramap[Instant](_.toString)

  /** also .map / .emap */
  implicit val decodeInstant: Decoder[Instant] =
    Decoder.decodeString.emapTry { str => Try(Instant.parse(str)) }

  val data = Instant.now()
  pprint.pprintln(data.asJson) // "2022-03-25T18:00:58.071796Z"

  val raw =
    """
      |"2022-03-25T18:00:58.071796Z"
      |""".stripMargin

  pprint.pprintln(decode[Instant](raw))

}
