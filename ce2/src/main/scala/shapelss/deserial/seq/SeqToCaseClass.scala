package shapelss.deserial.seq

import shapeless.Generic.Aux
import shapeless.Generic
import shapeless.HList
import shapeless.HNil
import shapeless.{:: => :#:}

import scala.collection.immutable.::

/** Seq[String] based implementation (without delimiter and parsing) Either-based result */
object SeqToCaseClass {

  type Result[+A] = Either[DecodeError, A]

  trait Reader[+A] { self =>
    def read(ss: Iterable[String]): Result[A]

    def map[B](f: A => B): Reader[B] = new Reader[B] {
      override def read(ss: Iterable[String]): Result[B] = self.read(ss).map(f)
    }
  }

  object Reader {
    val EMPTY = ""

    /** syntax to pick instance */
    private def pick[A](implicit ra: Reader[A]) = ra

    /** syntax to pick instance and read to Try[A] */
    def read[A: Reader](s: Iterable[String]): Result[A] = pick[A].read(s)

    /** given generic representation, pick corresponding reader, read, and map to the target type */
    def pickAndRead[A, HL: Reader](generic: Aux[A, HL]) = pick[HL].map(generic.from)

    private def head[A](as: Iterable[A]): Result[A] = as match {
      case Nil    => Left(DeSequenceIsEmpty)
      case h :: _ => Right(h)
      case _      => sys.error("designed for list...")
    }

    private def take(ss: Iterable[String]): (Iterable[String], Iterable[String]) = ss match {
      case Nil => (Nil, Nil)
      case _   => (Iterable(ss.head), ss.tail)
    }

    /** implicit instances */
    implicit val strReader: Reader[String] = new Reader[String] {
      def read(ss: Iterable[String]): Result[String] = head(ss)
    }
    implicit val intReader: Reader[Int] = new Reader[Int] {
      def read(ss: Iterable[String]): Result[Int] = head(ss).flatMap(_.toIntOption.toRight(DeInt))
    }
    implicit val longReader: Reader[Long] = new Reader[Long] {
      def read(ss: Iterable[String]): Result[Long] = head(ss).flatMap(_.toLongOption.toRight(DeLong))
    }
    implicit val dblReader: Reader[Double] = new Reader[Double] {
      def read(ss: Iterable[String]): Result[Double] = head(ss).flatMap(_.toDoubleOption.toRight(DeDouble))
    }
    implicit val bolReader: Reader[Boolean] = new Reader[Boolean] {
      def read(ss: Iterable[String]): Result[Boolean] = head(ss).flatMap(_.toBooleanOption.toRight(DeBoolean))
    }

    /** strict implementation based on absence of value to produce None */
    implicit def optReader[A: Reader]: Reader[Option[A]] = new Reader[Option[A]] {
      override def read(ss: Iterable[String]): Result[Option[A]] = head(ss).flatMap {
        case EMPTY => Right(None)
        case s     => Reader.read[A](Seq(s)).map(Some(_))
      }
    }
    implicit val hNilReader: Reader[HNil] = new Reader[HNil] {
      def read(ss: Iterable[String]) = Right(HNil)
    }
    implicit def hListReader[H: Reader, T <: HList: Reader]: Reader[H :#: T] = new Reader[H :#: T] {
      def read(ss: Iterable[String]): Result[H :#: T] = {
        val (head, tail) = take(ss)
        for {
          a <- Reader.read[H](head)
          b <- Reader.read[T](tail)
        } yield a :: b
      }
    }
  }

}
