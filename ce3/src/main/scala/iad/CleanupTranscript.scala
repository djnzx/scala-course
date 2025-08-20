package iad

import cats.effect._
import fs2.Stream
import fs2.io.file._

object CleanupTranscript extends IOApp.Simple {

  val path = "/Users/alexr/Library/CloudStorage/GoogleDrive-alexey.rykhalskiy@gmail.com/My Drive/_EU/2526s1/iad/lectures/transcript"

  val name = "iad02"

  val in = Path(s"$path/$name.txt")
  val out = Path(s"$path/$name.md")

  def mkSep(n: Int) = s"${"\n### -----"} #$n ${"-" * 50}"
  def mkNumber(rn: Ref[IO, Int]) = rn.getAndUpdate(_ + 1)

  val writePipe = Files[IO].writeUtf8Lines(out, Flags.Write)

  val sep = Set('#', '-')

  def run: IO[Unit] =
    Ref[IO].of(1).flatMap { rn =>
      Files[IO]
        .readUtf8Lines(in)
        .filter(_.nonEmpty)
        .filterNot(_.head.isDigit)
        .flatMap {
          case s if sep.contains(s.head) => Stream.eval(mkNumber(rn).map(mkSep))
          case s                         => Stream.emit(s)
        }
        .through(writePipe)
        .compile
        .drain
    }

}
