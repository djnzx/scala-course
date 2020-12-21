package shapelessx

import shapeless.Generic.Aux
import shapeless.{::, Generic, HList, HNil}

import scala.util.{Failure, Success, Try}

/**
  * original idea is taken here
  * https://stackoverflow.com/questions/20939716/idiomatic-scala-way-of-deserializing-delimited-strings-into-case-classes/20941316#20941316
  */
object StringToCaseApp extends App {

  trait Reader[+A] { self =>
    def read(s: String): Try[A]
    def map[B](f: A => B): Reader[B] = (s: String) => self.read(s).map(f)
  }

  object Reader {
    /** just syntax to pick instance */
    def pick[A: Reader] = implicitly[Reader[A]]
    /** just syntax to pick instance and read to Try[A] */
    def read[A: Reader](s: String): Try[A] = pick[A].read(s)
    /** given generic representation, pick corresponding reader, read, and map to the target type */
    def pickAndRead[A, HL: Reader](generic: Aux[A, HL]) = Reader.pick[HL].map(generic.from)
    /** implicit instances */
    implicit val strReader = new Reader[String] {
      def read(s: String) = Success(s)
    }
    implicit val intReader = new Reader[Int] {
      def read(s: String) = Try { s.toInt }
    }
    implicit val dblReader = new Reader[Double] {
      def read(s: String) = Try { s.toDouble }
    }
    implicit val bolReader = new Reader[Boolean] {
      def read(s: String) = Try { s.toBoolean }
    }
    implicit val hNilReader = new Reader[HNil] {
      def read(s: String) = s.isEmpty match {
        case true  => Success(HNil)
        case false => Failure(new Exception("Expect empty"))
      }
    }
    implicit def optReader[A: Reader] = new Reader[Option[A]] {
      override def read(s: String): Try[Option[A]] = s match {
        case "" => Success(None)
        case s  => Reader.read[A](s).map(Some[A])
      }
    }
    private def take(s: String): (String, String) = {
      val (head, rest) = s.span(_ != '|')
      val tail = rest match {
        case "" => ""
        case t  => t.tail
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

  case class TkyLine(a: String, b: String, c: Boolean, d: Option[Double])
  object TkyLine {
    implicit val reader = Reader.pickAndRead(Generic[TkyLine])
  }

  Reader.read[TkyLine]("a|bb|true|3.14") match {
    case Success(value) => pprint.pprintln(value)
    case Failure(x) => ???
  }

}
