package http_param_matcher

import io.circe.Decoder

object CircePlayground extends App {

  def decodeTypedOrDie[A: Decoder](raw: String): A =
    io.circe.parser.decode[A](SealedTraitDecoder.wrap(raw))
      .fold(_ => sys.error("can't decode"), identity)

  val x = decodeTypedOrDie[Fruit]("Apple")

}
