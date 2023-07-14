package ec2

import cats.effect.Blocker
import cats.effect.ContextShift
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeByName
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutorService
import scala.io.StdIn

object CommonParts {
  def printLine(s: String): IO[Unit] =
    for {
      _ <- IO(println(s))
      _ <- showThread("printLine")
    } yield ()

  def readLineBlocking: IO[String] = for {
    line <- IO(StdIn.readLine())
    _    <- showThread("readLineBlocking")
  } yield line

  def getFileSizeBlocking(path: String): IO[Long] =
    for {
      path <- IO(Paths.get(path))
      size <- IO(Files.size(path))
      _    <- showThread("getFileSizeBlocking")
    } yield size

  def getFileLinesBlocking(path: String): IO[Long] =
    for {
      _     <- IO.delay(Thread.sleep(1000))
      path  <- IO(Paths.get(path))
      lines <- IO(Files.lines(path).count())
      _     <- showThread("getFileLinesBlocking")
    } yield lines

  def showThread(name: String): IO[Unit] =
    IO.delay(
      println(
        s">> Operation ${Console.GREEN}$name${Console.RESET} executed on ${Console.RED}${Thread.currentThread().getName}${Console.RESET}>>"
      )
    )

  def checkIfExists(path: Path): IO[Boolean] = IO(Files.exists(path))

  def safeCreate(path: Path)(blocker: Blocker)(implicit cs: ContextShift[IO]): IO[Unit] = for {
    // we can block on io
    alreadyExists <- blocker.blockOn(checkIfExists(path))
    ctx: ExecutionContext = blocker.blockingContext
    // or create effect running on blocking EC
    _             <- blocker.delay[IO, Unit](Files.createFile(path)).unlessA(alreadyExists)
  } yield ()

  /** blocking context */
  val BlockingEC: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(
    Executors.newCachedThreadPool((r: Runnable) => {
      new Thread(r) {
        {
          setName(s"blocking-ec-$getName")
        }
      }
    })
  )

  /** custom blocking context */
  val customBlockingEC: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(
    Executors.newCachedThreadPool(r =>
      new Thread(r) {
        {
          setName(s"custom-blocking-ec-$getName")
        }
      }
    )
  )

}
