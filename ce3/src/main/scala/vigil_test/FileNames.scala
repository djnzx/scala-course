package vigil_test

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.kernel.Sync
import fs2._
import fs2.io._
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.{Path => JPath}
import scala.util.chaining.scalaUtilChainingOps

class FileNames[F[_]: Sync] {

  def stream(name: String) = Files
    .newDirectoryStream(Paths.get(name))
    .iterator()
    .pipe(fs2.Stream.unfoldEval(_)(it => Sync[F].delay(Option.when(it.hasNext)(it.next -> it))))

}

object FileNamesApp extends IOApp {

  // TODO: specify blocker
  def readFile(jPath: JPath) = file
    .Files[IO]
    .readAll(file.Path.fromNioPath(jPath))
    .through(text.utf8.decode)
    .through(text.lines)
    .drop(1)

  // TODO: specify blocker
  def writeFile(name: String, data: (Int, Int)) = {
    val (key, value) = data
    val contents = s"$key\t$value"
    val stream: Stream[Pure, String] = Stream.emit(contents)

    stream
      .through(text.utf8.encode)
      .through(file.Files[IO].writeAll(file.Path(name + "_out.tsv")))
      .compile
      .drain
  }

  def doTheLogic(jPath: JPath, fileType: FileType): IO[Unit] =
    readFile(jPath)
      .map(fileType.lineParser.parse)
      .unNone
      .compile
      // TODO: we can stream here if we have guarantee that all the values in order,
      //  or we can create manual collector
      .toList
      .map(Logic.process)
      .flatMap(data => writeFile(fileType.nameWithoutExtension(jPath.toString), data))

  def process(jPath: JPath): IO[Unit] = IO(pprint.pprintln(jPath)) >>
    FileType
      .detect(jPath.toString)
      .map { fileType => doTheLogic(jPath, fileType) }
      .getOrElse(IO.unit)

  def stream(path: String) = new FileNames[IO]
    .stream(path)
    .filter(!Files.isDirectory(_))
    .filter(p => FileType.detect(p.toString).isDefined)
    .evalTap(process)

  def checkParamsAndRun(args: List[String]) = args match {
    case in :: _ => stream(in).compile.drain.as(ExitCode.Success)
    case _       => IO(pprint.pprintln("Source path expected")).as(ExitCode.Error)
  }

  override def run(args: List[String]): IO[ExitCode] =
    checkParamsAndRun(args)

}
