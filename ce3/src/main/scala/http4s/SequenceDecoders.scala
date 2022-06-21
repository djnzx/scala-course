package http4s

import cats.data.NonEmptyList
import cats.implicits._
import org.http4s.ParseFailure
import org.http4s.QueryParamDecoder
import org.http4s.QueryParameterValue

object SequenceDecoders {

  implicit def listQueryParamDecoder[A: QueryParamDecoder]: QueryParamDecoder[List[A]] = pv =>
    pv.value.split(",").toList.traverse(s => QueryParamDecoder[A].decode(QueryParameterValue(s)))

  implicit def nelQueryParamDecoder[A: QueryParamDecoder]: QueryParamDecoder[NonEmptyList[A]] =
    listQueryParamDecoder[A]
      .emap(_.toNel.toRight(ParseFailure("shouldn't be empty", "")))

  implicit def setQueryParamDecoder[A: QueryParamDecoder]: QueryParamDecoder[Set[A]] =
    listQueryParamDecoder[A].map(_.toSet)

}
