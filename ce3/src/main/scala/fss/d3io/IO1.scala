package fss.d3io

import cats._
import cats.effect._
import cats.effect.implicits._
import cats.implicits._
import fs2._
import fs2.io._
import fs2.io.file._
import java.nio.file.Paths

/** https://fs2.io/#/io */
object IO1 extends IOApp.Simple {

  def readFileToStream[F[_]: Files](path: Path): Stream[F, Byte] = Files[F].readAll(path)
  def writeStreamToPath[F[_]: Files](target: Path): Pipe[F, Byte, INothing] = Files[F].writeAll(target)

  def writeDigest[F[_]: Files: Concurrent](path: Path): F[Unit] = {
    val target = Path(path.toString + ".sha256")

    readFileToStream[F](path)
      .through(hash.sha256)
      .through(text.hex.encode)
      .through(text.utf8.encode)
      .through(writeStreamToPath(target))
      .compile
      .drain
  }

  override def run: IO[Unit] = {
    val np = Paths.get(getClass.getClassLoader.getResource("1.txt").getFile)
    writeDigest[IO](Path.fromNioPath(np))
  }

}
