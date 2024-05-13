package json

import zio.json.DeriveJsonDecoder
import zio.json.DeriveJsonEncoder
import zio.json.JsonDecoder
import zio.json.JsonEncoder

case class Value[A, C[_]](value: C[A])
object Value {

  implicit def decoder[A: JsonDecoder, C[_]](implicit
    ev: JsonDecoder[C[A]]
  ): JsonDecoder[Value[A, C]] = DeriveJsonDecoder.gen[Value[A, C]]

  implicit def encoder[A: JsonEncoder, C[_]](implicit
    ev: JsonEncoder[C[A]]
  ): JsonEncoder[Value[A, C]] = DeriveJsonEncoder.gen[Value[A, C]]

}
