package fss.d3io

import cats.effect._
import fs2.Stream
import fs2.io.file._
import fs2.text
import io.circe.fs2._
import io.circe.generic.auto._
import io.circe.jawn.decode
import io.circe.parser.parse
import java.nio.file.Paths

/** https://github.com/circe/circe-fs2 */
object IO2 extends IOApp.Simple {

  def lines[F[_]: Files: Sync](path: Path) =
    Files[F]
      .readAll(path)
      .through(text.utf8.decode)
      .through(text.lines)

  /** file to Stream[Json], it will fail after first error, because handling is out of the scope `through` */
  def app1[F[_]: Files: Sync](path: Path) =
    lines(path)
      .through(stringStreamParser)

  /** file to Stream[Json], erorr will be handled but processing will be stopped after the error */
  def app2[F[_]: Files: Sync](path: Path) =
    lines(path)
      .through(stringStreamParser)
      .attempt
      .flatMap(_.fold(_ => Stream.empty, Stream.emit))

  /** file to Stream[Json], errors handled manually, errors will be skipped */
  def app3[F[_]: Files: Sync](path: Path) =
    lines(path)
      .map(parse)
      .flatMap(_.fold(_ => Stream.empty, Stream.emit))

  case class Foo(repo: String, stars: Int)

  /** file to Stream[Foo], errors handled manually, errors will be skipped */
  def app4[F[_]: Files: Sync](path: Path) =
    lines(path)
      .map(decode[Foo])
      .flatMap(_.fold(_ => Stream.empty, Stream.emit))

  /** file to Stream[Foo], process will be stopped after the first error */
  def app5[F[_]: Files: Sync](path: Path) =
    lines(path)
      .through(stringStreamParser)
      .through(decoder[F, Foo])
      .attempt
      .flatMap(_.fold(_ => Stream.empty, Stream.emit))

  override def run: IO[Unit] = {
    val np = Paths.get(getClass.getClassLoader.getResource("1lines.json").getFile)
    app4[IO](Path.fromNioPath(np))
      .evalTap(x => IO.delay(pprint.pprintln(x)))
      .compile
      .drain
  }

}
