package genrec

import cats.implicits.catsSyntaxOptionId
import org.apache.avro.generic.GenericRecord

trait TypeExtractor[A] {
  def extract(x: Any): Option[A]
}

object TypeExtractor {

  type Node = GenericRecord

  implicit val typeExtractorBool: TypeExtractor[Boolean] = {
    case x: Boolean => x.some
    case _          => None
  }

  implicit val typeExtractorChar: TypeExtractor[Char] = {
    case x: Char => x.some
    case _       => None
  }

  implicit val typeExtractorLong: TypeExtractor[Long] = {
    case x: Long  => x.some
    case x: Int   => x.toLong.some
    case x: Short => x.toLong.some
    case x: Byte  => x.toLong.some
    case _        => None
  }

  implicit val typeExtractorInt: TypeExtractor[Int] = {
    case x: Int   => x.some
    case x: Short => x.toInt.some
    case x: Byte  => x.toInt.some
    case _        => None
  }

  implicit val typeExtractorShort: TypeExtractor[Short] = {
    case x: Short => x.some
    case x: Byte  => x.toShort.some
    case _        => None
  }

  implicit val typeExtractorByte: TypeExtractor[Byte] = {
    case x: Byte => x.some
    case _       => None
  }

  implicit val typeExtractorFloat: TypeExtractor[Float] = {
    case x: Float => x.some
    case _        => None
  }

  implicit val typeExtractorDouble: TypeExtractor[Double] = {
    case x: Double => x.some
    case x: Float  => x.toDouble.some
    case _         => None
  }

  implicit val typeExtractorString: TypeExtractor[String] = {
    case x: String => x.some
    case _         => None
  }

  implicit val typeExtractorNode: TypeExtractor[Node] = {
    case x: Node => x.some
    case _       => None
  }

}
