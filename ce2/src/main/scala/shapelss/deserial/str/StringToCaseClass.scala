package shapelss.deserial.str

import shapeless.Generic.Aux
import shapeless.::
import shapeless.HList
import shapeless.HNil
import shapelss.deserial.seq._

import scala.util.Try

/** String-parsing with delimiter based implementation Try-based result
  */
object StringToCaseClass {

  type Result[+A] = Either[DecodeError, A]

  trait Reader[+A] { self =>
    def read(s: String): Result[A]

    def map[B](f: A => B): Reader[B] = new Reader[B] {
      override def read(s: String): Result[B] = self.read(s).map(f)
    }
  }

  object Reader {
    val EMPTY = ""
    val DELIM = '|'

    /** just syntax to pick instance */
    def pick[A: Reader] = implicitly[Reader[A]]

    /** just syntax to pick instance and read to Try[A] */
    def read[A: Reader](s: String): Result[A] = pick[A].read(s)

    /** given generic representation, pick corresponding reader, read, and map to the target type */
    def pickAndRead[A, HL: Reader](generic: Aux[A, HL]) = Reader.pick[HL].map(generic.from)

    /** implicit instances */
    implicit val strReader = new Reader[String] {
      def read(s: String) = Right(s)
    }
    implicit val intReader = new Reader[Int] {
      def read(s: String) = s.toIntOption.toRight(DeInt)
    }
    implicit val dblReader = new Reader[Double] {
      def read(s: String) = s.toDoubleOption.toRight(DeDouble)
    }
    implicit val bolReader = new Reader[Boolean] {
      def read(s: String) = s.toBooleanOption.toRight(DeBoolean)
    }
    implicit val hNilReader = new Reader[HNil] {
      def read(s: String) = s.isEmpty match {
        case true  => Right(HNil)
        case false => Left(DeSequenceNonEmpty)
      }
    }
    implicit def optReader[A: Reader] = new Reader[Option[A]] {
      override def read(s: String) = s match {
        case "" => Right(None)
        case s  => Reader.read[A](s).map(Some[A])
      }
    }
    private def take(s: String): (String, String) = {
      val (head, rest) = s.span(_ != DELIM)
      val tail = rest match {
        case e @ EMPTY => e
        case t         => t.tail
      }
      (head, tail)
    }
    implicit def hListReader[H: Reader, T <: HList: Reader]: Reader[H :: T] = new Reader[H :: T] {
      def read(s: String) = {
        val (head: String, tail: String) = take(s)
        for {
          a <- Reader.read[H](head)
          b <- Reader.read[T](tail)
        } yield a :: b
      }
    }
  }

}
