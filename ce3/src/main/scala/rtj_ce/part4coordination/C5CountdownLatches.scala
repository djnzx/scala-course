package rtj_ce.part4coordination

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.std.CountDownLatch
import cats.syntax.parallel._
import cats.syntax.traverse._
import java.io.File
import java.io.FileWriter
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random
import utils.DebugWrapper

/**   - CDLatches are a coordination primitive initialized with a count.
  *   - All fibers calling await() on the C5CDLatch are (semantically) blocked.
  *   - When the internal count of the latch reaches 0 (via release() calls from other fibers), all waiting fibers are
  *     unblocked.
  *
  * There are two main use cases:
  *   - 1. many workers, each of them does .release in the end and main thread await, until all finish
  *   - 2. many processes await to start and one process releases all
  */
object C5CountdownLatches extends IOApp.Simple {

  def announcer(latch: CountDownLatch[IO]): IO[Unit] = for {
    _ <- IO("Starting race shortly...").debug0 >> IO.sleep(2.seconds)
    _ <- IO("5...").debug0 >> IO.sleep(1.second)
    _ <- latch.release
    _ <- IO("4...").debug0 >> IO.sleep(1.second)
    _ <- latch.release
    _ <- IO("3...").debug0 >> IO.sleep(1.second)
    _ <- latch.release
    _ <- IO("2...").debug0 >> IO.sleep(1.second)
    _ <- latch.release
    _ <- IO("1...").debug0 >> IO.sleep(1.second)
    _ <- latch.release // gun firing
    _ <- IO("GO GO GO!").debug0
  } yield ()

  def createRunner(id: Int, latch: CountDownLatch[IO]): IO[Unit] = for {
    _ <- IO(s"[runner $id] waiting for signal...").debug0
    _ <- latch.await // block this fiber until the count reaches 0
    _ <- IO(s"[runner $id] RUNNING!").debug0
  } yield ()

  def sprint(): IO[Unit] = for {
    latch        <- CountDownLatch[IO](5)
    announcerFib <- announcer(latch).start
    _            <- (1 to 10).toList.parTraverse(id => createRunner(id, latch))
    _            <- announcerFib.join
  } yield ()

  /** Exercise: simulate a file downloader on multiple threads */
  object FileServer {
    val fileChunksList = Array(
      "I love Scala.",
      "Cats Effect seems quite fun.",
      "Never would I have thought I would do low-level concurrency WITH pure FP."
    )

    def getNumChunks: IO[Int] = IO(fileChunksList.length)
    def getFileChunk(n: Int): IO[String] = IO(fileChunksList(n))
  }

  def writeToFile(path: String, contents: String): IO[Unit] = {
    val fileResource = Resource.make(IO(new FileWriter(new File(path))))(writer => IO(writer.close()))
    fileResource.use { writer =>
      IO(writer.write(contents))
    }
  }

  def appendFileContents(fromPath: String, toPath: String): IO[Unit] = {
    val compositeResource = for {
      reader <- Resource.make(IO(Source.fromFile(fromPath)))(source => IO(source.close()))
      writer <- Resource.make(IO(new FileWriter(new File(toPath), true)))(writer => IO(writer.close()))
    } yield (reader, writer)

    compositeResource.use { case (reader, writer) =>
      IO(reader.getLines().foreach(writer.write))
    }
  }

  def createFileDownloaderTask(id: Int, latch: C5CDLatch, filename: String, destFolder: String): IO[Unit] = for {
    _     <- IO(s"[task $id] downloading chunk...").debug0
    _     <- IO.sleep((Random.nextDouble() * 1000).toInt.millis)
    chunk <- FileServer.getFileChunk(id)
    _     <- writeToFile(s"$destFolder/$filename.part$id", chunk)
    _     <- IO(s"[task $id] chunk download complete").debug0
    _     <- latch.release
  } yield ()

  /*
    - call file server API and get the number of chunks (n)
    - start a C5CDLatch
    - start n fibers which download a chunk of the file (use the file server's download chunk API)
    - block on the latch until each task has finished
    - after all chunks are done, stitch the files together under the same file on disk
   */
  def downloadFile(filename: String, destFolder: String): IO[Unit] = for {
    n     <- FileServer.getNumChunks
    latch <- C5CDLatch(n)
    _     <- IO(s"Download started on $n fibers.").debug0
    _     <- (0 until n).toList.parTraverse(id => createFileDownloaderTask(id, latch, filename, destFolder))
    _     <- latch.await
    _     <- (0 until n).toList
               .traverse(id => appendFileContents(s"$destFolder/$filename.part$id", s"$destFolder/$filename"))
  } yield ()

  override def run = downloadFile("myScalafile.txt", "cats-effect/src/main/resources")
}
