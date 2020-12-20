package shapelessx

import shapeless.{::, Generic, HList, HNil}

import scala.util.{Failure, Success, Try}

object SeqToCaseApp extends App {
  
  trait Reader[+A] {// self =>
    def read(s: String): Try[A]
    def map[B](f: A => B): Reader[B] = (s: String) => read(s).map(f)
  }

  object Reader {
//    def apply[A: Reader]: Reader[A] = 
    def read[A: Reader](s: String): Try[A] = implicitly[Reader[A]].read(s)

    implicit object StringReader extends Reader[String] {
      def read(s: String) = Success(s)
    }
    implicit object IntReader extends Reader[Int] {
      def read(s: String) = Try { s.toInt }
    }

    implicit object HNilReader extends Reader[HNil] {
      def read(s: String) =
        if (s.isEmpty()) Success(HNil)
        else Failure(new Exception("Expect empty"))
    }
    implicit def HListReader[A : Reader, H <: HList : Reader] : Reader[A :: H] = new Reader[A :: H] {
      def read(s: String) = {
        val (before, colonAndBeyond) = s.span(_ != ':')
        val after = if (colonAndBeyond.isEmpty()) "" else colonAndBeyond.tail
        for {
          a <- Reader.read[A](before)
          b <- Reader.read[H](after)
        } yield a :: b
      }
    }

  }

  case class TkyLine(a: String, b: String, c: String, d: String)
  object TkyLine {
    implicit val tkyStringReader: Reader[TkyLine] =
      implicitly[Reader[String :: String :: String :: String :: HNil]]
        .map(s => Generic[TkyLine].from(s))
  }

  val parsed = Reader.read[TkyLine]("a:bb:ccc:dddd") 
  parsed match {
    case Success(value) => pprint.pprintln(value)
    case Failure(exception) => ???
  }

  val data: Seq[String] = List("a", "bb", "ccc", "dddd")

}
