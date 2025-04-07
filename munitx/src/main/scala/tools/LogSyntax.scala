package tools

import cats.effect._

trait LogSyntax {

  def log[A](a: A)(implicit l: sourcecode.Line, f: sourcecode.FileName) = pprint.log(a)
  def logF[A](a: A)(implicit l: sourcecode.Line, f: sourcecode.FileName) = IO(log(a))

  implicit class LogSyntax[A](fa: IO[A]) {
    def log(implicit l: sourcecode.Line, f: sourcecode.FileName) = fa.flatMap(logF)
  }

}
