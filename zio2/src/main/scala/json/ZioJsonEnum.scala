package json

import enumeratum._
import zio.json.JsonDecoder
import zio.json.JsonEncoder

trait ZioJsonEnum[A <: EnumEntry] { this: Enum[A] =>

  private final val myName = this.getClass.getSimpleName.stripSuffix("$")

  implicit val encoder: JsonEncoder[A] =
    JsonEncoder.string.contramap[A](a => a.entryName)

  implicit val decoder: JsonDecoder[A] =
    JsonDecoder.string
      .mapOrFail(s => withNameOption(s).toRight(s"$myName: no such member `$s`"))

}
