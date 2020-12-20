package shapelessx

import shapeless.{::, Generic, HList, HNil}

import scala.util.{Failure, Success, Try}

object SeqToCaseApp extends App {

  trait Reader[+A] {
    def read(s: String): Try[A]
  }

  object Reader {
    def read[A: Reader](s: String): Try[A] = implicitly[Reader[A]].read(s)

    implicit val strReader = new Reader[String] {
      def read(s: String) = Success(s)
    }
    implicit val intReader = new Reader[Int] {
      def read(s: String) = Try { s.toInt }
    }
    implicit val hNilReader = new Reader[HNil] {
      def read(s: String) = s.isEmpty match {
        case true  => Success(HNil)
        case false => Failure(new Exception("Expect empty"))
      }
    }
    private def take(s: String): (String, String) = {
      val (head, rest) = s.span(_ != ':')
      val tail = rest match {
        case "" => ""
        case t  => t.tail
      }
      (head, tail)
    } 
    implicit def hListReader[A: Reader, T <: HList: Reader]: Reader[A :: T] = new Reader[A :: T] {
      def read(s: String) = {
        val (head, tail) = take(s)
        for {
          a <- Reader.read[A](head)
          b <- Reader.read[T](tail)
        } yield a :: b
      }
    }

  }

  case class TkyLine(a: String, b: String, c: String, d: String)
  object TkyLine {
    implicit val reader: Reader[TkyLine] = new Reader[TkyLine] {
      override def read(s: String): Try[TkyLine] =
        implicitly[Reader[String :: String :: String :: String :: HNil]]
          .read(s)
          .map(Generic[TkyLine].from)
    } 
  }

  val parsed = Reader.read[TkyLine]("a:bb:ccc:dddd")
  parsed match {
    case Success(value) => pprint.pprintln(value)
    case Failure(exception) => ???
  }

  val data: Seq[String] = List("a", "bb", "ccc", "dddd")

}
