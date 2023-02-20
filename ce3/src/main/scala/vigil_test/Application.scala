package vigil_test

import cats.effect.kernel._
import cats.implicits._
import fs2._
import fs2.io._
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.{Path => JPath}
import scala.util.chaining.scalaUtilChainingOps

class Application[F[_]: Sync](implicit f: file.Files[F]) {

  private def streamFolderFileNames(name: String) =
    Files
      .newDirectoryStream(Paths.get(name))
      .iterator()
      .pipe(Stream.unfoldEval(_)(it => F.delay(Option.when(it.hasNext)(it.next -> it))))
      .evalFilter(p => F.delay(!Files.isDirectory(p)))
      .filter(p => FileType.detect(p.toString).isDefined)

  // TODO: specify blocker to run on the different ExecutionContext
  private def readFileContents(jPath: JPath) =
    file
      .Files[F]
      .readAll(file.Path.fromNioPath(jPath))
      .through(text.utf8.decode)
      .through(text.lines)
      .drop(1)

  // TODO: specify blocker to run on the different ExecutionContext
  private def writeFileContents(dstPath: String, name: String, data: (Int, Int)) = {
    val (key, value) = data
    val contents = s"$key\t$value"
    val target = file.Path.fromNioPath(Paths.get(dstPath, s"$name.tsv"))

    Stream.emit(contents)
      .through(text.utf8.encode)
      .through(file.Files[F].writeAll(target))
      .compile
      .drain
  }

  private def doTheLogic(srcPath: JPath, fileType: FileType, dstPath: String) =
    readFileContents(srcPath)
      .map(fileType.lineParser.parse)
      .unNone
      .compile
      // TODO: 1. we can make it using O(N values)
      //  by writing custom collector, which will count during collection
      // TODO: 2. we can stream here and use O(1) memory
      //  but only in case we have guarantee that all the values in order
      .toList
      .map(Logic.process)
      .flatMap { data =>
        val name = fileType.nameWoExt(srcPath.getFileName.toString)
        writeFileContents(dstPath, name, data)
      }

  private def processOne(srcPath: JPath, dstPath: String) =
    FileType
      .detect(srcPath.toString)
      .map { fileType => doTheLogic(srcPath, fileType, dstPath) }
      .getOrElse(F.unit)

  def run(srcPath: String, dstPath: String)(implicit ev: Concurrent[F]) =
    streamFolderFileNames(srcPath)
      .parEvalMap(10)(srcPath => processOne(srcPath, dstPath))
      .compile
      .drain

}

object Application {

  def go[F[_]: Sync: Concurrent: file.Files](srcPath: String, dstPath: String) =
    new Application[F].run(srcPath, dstPath)

}
