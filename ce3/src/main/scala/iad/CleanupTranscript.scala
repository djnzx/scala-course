package iad

import cats.effect._
import fs2.Stream
import fs2.io.file._

object CleanupTranscript extends IOApp.Simple {

  val in = Path("/Users/alexr/Downloads/in.txt")
  val out = Path("/Users/alexr/Downloads/out.txt")

  def mkSep(n: Int) = s"${"-" * 5} #$n ${"-" * 50}"
  def mkNumber(rn: Ref[IO, Int]) = rn.getAndUpdate(_ + 1)

  val writePipe = Files[IO].writeUtf8Lines(out, Flags.Write)

  def run: IO[Unit] =
    Ref[IO].of(1).flatMap { rn =>
      Files[IO]
        .readUtf8Lines(in)
        .filter(_.nonEmpty)
        .filterNot(_.head.isDigit)
        .filterNot(_.head == '#')
        .flatMap {
          case s if s.head == '-' => Stream.eval(mkNumber(rn).map(mkSep))
          case s                  => Stream.emit(s)
        }
        .through(writePipe)
        .compile
        .drain
    }

}
