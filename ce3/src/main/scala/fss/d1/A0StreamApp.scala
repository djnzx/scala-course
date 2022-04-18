package fss.d1

import cats.effect.IO
import cats.effect.IOApp
import fs2.INothing
import fs2.Pipe
import fs2.Stream
import fs2.io.file.Files
import fs2.io.file.Path
import fs2.text

object A0StreamApp extends IOApp.Simple {

  val fileName = A0Logic.evalFileName("data_c.txt")

  val writeBytes: Pipe[IO, Byte, INothing] =
    Files[IO]
      .writeAll(Path("fs2s2ce3/src/main/resources/data_f.txt"))

  val stream: Stream[IO, INothing] =
    Files[IO]
      .readAll(Path(fileName))
      .through(text.utf8.decode)
      .through(text.lines)
      .filter(s => s.trim.nonEmpty && !s.startsWith("//"))
      .map(line => A0Logic.cToF(line.toDouble).toString)
      .intersperse("\n")
      .through(text.utf8.encode)
      .through(writeBytes)

  override def run: IO[Unit] =
    stream.compile.drain
}
