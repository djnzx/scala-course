package shapelessx.deserial.seq

import shapeless.Generic.Aux
import shapeless.{::, HList, HNil}

import scala.collection.immutable.{:: => <::>}

/**
  * Seq[String] based implementation (without delimiter and parsing)
  * Either-based result
  */
object SeqToCaseClass {

  trait TransformError
  case object TeSequenceIsEmpty extends TransformError
  case object TeSequenceIsNotEmpty extends TransformError
  case object TeIntTransform extends TransformError
  case object TeDoubleTransform extends TransformError
  case object TeBooleanTransform extends TransformError
  
  type Result[+A] = Either[TransformError, A]
  
  trait Reader[+A] { self =>
    def read(ss: Seq[String]): Result[A]

    def map[B](f: A => B): Reader[B] = new Reader[B] {
      override def read(ss: Seq[String]): Result[B] = self.read(ss).map(f)
    }
  }

  object Reader {
    val EMPTY = ""

    /** just syntax to pick instance */
    def pick[A: Reader] = implicitly[Reader[A]]
    /** just syntax to pick instance and read to Try[A] */
    def read[A: Reader](s: Seq[String]): Result[A] = pick[A].read(s)
    /** given generic representation, pick corresponding reader, read, and map to the target type */
    def pickAndRead[A, HL: Reader](generic: Aux[A, HL]) = Reader.pick[HL].map(generic.from)

    def head[A](sa: Seq[A]): Result[A] = sa match {
      case Nil      => Left(TeSequenceIsEmpty)
      case h <::> _ => Right(h)
    }
    
    /** implicit instances */
    implicit val strReader = new Reader[String] {
      def read(ss: Seq[String]) = head(ss)
    }
    implicit val intReader = new Reader[Int] {
      def read(ss: Seq[String]) = head(ss).flatMap(_.toIntOption.toRight(TeIntTransform))
    }
    implicit val dblReader = new Reader[Double] {
      def read(ss: Seq[String]) = head(ss).flatMap(_.toDoubleOption.toRight(TeDoubleTransform))
    }
    implicit val bolReader = new Reader[Boolean] {
      def read(ss: Seq[String]) = head(ss).flatMap(_.toBooleanOption.toRight(TeBooleanTransform))
    }
    implicit val hNilReader = new Reader[HNil] {
      def read(ss: Seq[String]) = ss match {
        case Nil => Right(HNil)
        case _   => Left(TeSequenceIsNotEmpty)
      }
    }
    implicit def optReader[A: Reader] = new Reader[Option[A]] {
      override def read(ss: Seq[String]) = head(ss).flatMap {
        case EMPTY => Right(None)
        case s     => Reader.read[A](Seq(s)).map(Some[A])
      } 
    }
    private def take(ss: Seq[String]): (Seq[String], Seq[String]) = ss match {
      case Nil    => (Nil, Nil)
      case h <::> t => (Seq(h), t)
    } 
    implicit def hListReader[H: Reader, T <: HList : Reader]: Reader[H :: T] = new Reader[H :: T] {
      def read(ss: Seq[String]) = {
        val (head, tail) = take(ss)
        for {
          a <- Reader.read[H](head)
          b <- Reader.read[T](tail)
        } yield a :: b
      }
    }
  }

}
