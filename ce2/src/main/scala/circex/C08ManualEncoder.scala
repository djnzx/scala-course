package circex

import io.circe
import io.circe.jawn.decode
import io.circe.Decoder
import io.circe.HCursor

object C08ManualEncoder extends App {

  case class Thing(name: String, value: Int)

  implicit val decodeFoo: Decoder[Thing] = new Decoder[Thing] {
    final def apply(c: HCursor): Decoder.Result[Thing] =
      for {
        foo <- c.downField("name").as[String]
        bar <- c.downField("value").as[Int]
      } yield Thing(foo, bar)
  }

  val raw =
    """
      |{"name":"Hello","value":44}
      |""".stripMargin

  val decoded: Either[circe.Error, Thing] = decode[Thing](raw)
  pprint.pprintln(decoded)
}
