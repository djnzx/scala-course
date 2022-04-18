package rtj_ce.part4coordination

import cats.effect._
import cats.effect.kernel.Outcome
import cats.syntax.traverse._
import scala.concurrent.duration._
import utils._

object C2DefersNotifier extends IOApp.Simple {

  // simulate downloading some content
  val fileParts = List("I ", "love S", "cala", " with Cat", "s Effect!<EOF>")

  /** busy wait implementation, we constantly check the result */
  def fileNotifierWithRef(): IO[Unit] = {
    def downloadFile(contentRef: Ref[IO, String]): IO[Unit] =
      fileParts
        .map { part =>
          IO(s"[downloader] got '$part'").debug >> IO.sleep(1.second) >> contentRef.update(currentContent => currentContent + part)
        }
        .sequence
        .void

    def notifyFileComplete(contentRef: Ref[IO, String]): IO[Unit] = for {
      file <- contentRef.get
      _    <-
        if (file.endsWith("<EOF>")) IO("[notifier] File download complete").debug
        else
          IO("[notifier] downloading...").debug >> IO.sleep(500.millis) >> notifyFileComplete(contentRef) // busy wait!
    } yield ()

    for {
      contentRef    <- Ref[IO].of("")
      fibDownloader <- downloadFile(contentRef).start
      notifier      <- notifyFileComplete(contentRef).start
      _             <- fibDownloader.join
      _             <- notifier.join
    } yield ()
  }

  /** deferred works miracles for waiting */
  def fileNotifierWithDeferred(): IO[Unit] = {
    def notifyFileComplete(signal: Deferred[IO, String]): IO[Unit] = for {
      _ <- IO("[notifier] downloading...").debug
      _ <- signal.get // blocks until the signal is completed
      _ <- IO("[notifier] File download complete").debug
    } yield ()

    def downloadFilePart(part: String, contentRef: Ref[IO, String], signal: Deferred[IO, String]): IO[Unit] = for {
      _             <- IO(s"[downloader] got '$part'").debug
      _             <- IO.sleep(1.second)
      latestContent <- contentRef.updateAndGet(currentContent => currentContent + part)
      _             <- if (latestContent.contains("<EOF>")) signal.complete(latestContent) else IO.unit
    } yield ()

    for {
      contentRef   <- Ref[IO].of("")
      signal       <- Deferred[IO, String]
      notifierFib  <- notifyFileComplete(signal).start
      fileTasksFib <- fileParts.map(part => downloadFilePart(part, contentRef, signal)).sequence.start
      _            <- notifierFib.join
      _            <- fileTasksFib.join
    } yield ()
  }

  override def run = fileNotifierWithDeferred()
}
