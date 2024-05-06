package circe101.c9_enriched_decoder

import cats.implicits.catsSyntaxOptionId
import io.circe.{Decoder, Encoder, HCursor}
import io.circe.syntax.EncoderOps

object DecoderEnriched {

  type DecoderFn[A] = HCursor => Decoder.Result[A]

  implicit class DecoderOps[A](val da: Decoder[A]) extends AnyVal {

    private def addFieldValuePair[B: Encoder](cursor: HCursor, fieldName: String, fieldValue: B): HCursor =
      cursor
        .focus
        .map(_.deepMerge(Map(fieldName -> fieldValue).asJson).hcursor)
        .getOrElse(cursor)

    def ifAbsentCompute[B: Encoder](fieldName: String)(fieldValueProvider: DecoderFn[B]): Decoder[A] =
      (cursor: HCursor) =>
        cursor.downField(fieldName).succeeded match {

          /** if field is absent */
          case false =>
            fieldValueProvider(cursor)
              .flatMap(b => da(addFieldValuePair(cursor, fieldName, b)))

          /** if field is present */
          case _ => da(cursor)

        }

    def ifAbsentProvide[B: Encoder](fieldName: String, fieldValue: => B): Decoder[A] =
      ifAbsentCompute(fieldName)(_ => Right(fieldValue))

    def ifPresentCompute[B: Encoder](fieldName: String)(fieldValueProvider: DecoderFn[B]): Decoder[A] =
      (cursor: HCursor) =>
        fieldValueProvider(cursor) match {

          /** if value is provided, then try to decode with added (k, v) to the cursor */
          case Right(b) => da(addFieldValuePair(cursor, fieldName, b))

          /** if value isn't provided, fallback to default implementation */
          case _ => da(cursor)
        }

    def ifPresentOverride[B: Encoder](fieldName: String)(fieldValue: => B): Decoder[A] =
      ifPresentCompute(fieldName)(_ => Right(fieldValue))

  }

  implicit class DecoderResultOps[A](val result: Decoder.Result[A]) extends AnyVal {
    def asRightOpt: Decoder.Result[Option[A]] = result.map(_.some)
  }
}
