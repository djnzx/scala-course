package skunkx

import cats.effect._
import cats.implicits._

trait Tools {

  def log[A](a: A)(implicit l: sourcecode.Line, f: sourcecode.FileName) = pprint.log(a)
  def logF[A](a: A)(implicit l: sourcecode.Line, f: sourcecode.FileName) = IO(log(a))

  implicit class LogSyntax[A](fa: IO[A]) {
    def log(implicit l: sourcecode.Line, f: sourcecode.FileName) = fa.flatMap(logF)
    def logXs[B](implicit ev: A <:< Iterable[B], l: sourcecode.Line, f: sourcecode.FileName) =
      fa.map(_.toList)
        .flatMap(_.traverse(logF))
  }

}
