package shapelessx.deserial.seq

import shapeless.Generic.Aux
import shapeless.{::, HList, HNil}

import scala.collection.immutable.{:: => <>}
import scala.util.{Failure, Success, Try}

/**
  * Seq[String] based implementation (without delimiter and parsing)
  * Try-based result. TODO: implement it with Either
  */
object SeqToCaseClass {
  
  trait Reader[+A] { self =>
    def read(ss: Seq[String]): Try[A]

    def map[B](f: A => B): Reader[B] = new Reader[B] {
      override def read(ss: Seq[String]): Try[B] = self.read(ss).map(f)
    }
  }

  object Reader {
    val EMPTY = ""

    /** just syntax to pick instance */
    def pick[A: Reader] = implicitly[Reader[A]]
    /** just syntax to pick instance and read to Try[A] */
    def read[A: Reader](s: Seq[String]): Try[A] = pick[A].read(s)
    /** given generic representation, pick corresponding reader, read, and map to the target type */
    def pickAndRead[A, HL: Reader](generic: Aux[A, HL]) = Reader.pick[HL].map(generic.from)

    def head[A](sa: Seq[A]): Try[A] = sa match {
      case Nil    => Failure(new Exception("Seq is empty during entity decoding"))
      case h <> _ => Success(h)
    }
    
    /** implicit instances */
    implicit val strReader = new Reader[String] {
      def read(ss: Seq[String]) = head(ss)
    }
    implicit val intReader = new Reader[Int] {
      def read(ss: Seq[String]) = head(ss).flatMap(x => Try(x.toInt))
    }
    implicit val dblReader = new Reader[Double] {
      def read(ss: Seq[String]) = head(ss).flatMap(x => Try(x.toDouble))
    }
    implicit val bolReader = new Reader[Boolean] {
      def read(ss: Seq[String]) = head(ss).flatMap(x => Try(x.toBoolean))
    }
    implicit val hNilReader = new Reader[HNil] {
      def read(ss: Seq[String]) = ss match {
        case Nil => Success(HNil)
        case _   => Failure(new Exception("Seq is non-empty in the end of parsing"))
      }
    }
    implicit def optReader[A: Reader] = new Reader[Option[A]] {
      override def read(ss: Seq[String]): Try[Option[A]] = head(ss).flatMap {
        case EMPTY => Success(None)
        case s     => Reader.read[A](Seq(s)).map(Some[A])
      } 
    }
    private def take(ss: Seq[String]): (Seq[String], Seq[String]) = ss match {
      case Nil    => (Nil, Nil)
      case h <> t => (Seq(h), t)
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
