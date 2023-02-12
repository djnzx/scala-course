package rtj_ce.part3concurrency

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.kernel.Outcome.Canceled
import cats.effect.kernel.Outcome.Errored
import cats.effect.kernel.Outcome.Succeeded
import java.io.File
import java.io.FileReader
import java.util.Scanner
import scala.concurrent.duration._
import utils._

object C2BracketResourcesGuarantee extends IOApp.Simple {

  // use-case: manage a connection lifecycle
  class Connection(url: String) {
    def open(): IO[String] = IO(s"opening connection to $url").debug0
    def close(): IO[String] = IO(s"closing connection to $url").debug0
  }

  val asyncFetchUrl = for {
    fib <- (new Connection("rockthejvm.com").open() *> IO.sleep((Int.MaxValue).seconds)).start
    _   <- IO.sleep(1.second) *> fib.cancel
  } yield ()
  // problem: leaking resources

  val correctAsyncFetchUrl = for {
    conn <- IO(new Connection("rockthejvm.com"))
    fib  <- (conn.open() *> IO.sleep((Int.MaxValue).seconds)).onCancel(conn.close().void).start
    _    <- IO.sleep(1.second) *> fib.cancel
  } yield ()

  /*
    bracket pattern: someIO.bracket(useResourceCb)(releaseResourceCb)
    bracket is equivalent to try-catches (pure FP)
   */
  val bracketFetchUrl = IO(new Connection("rockthejvm.com"))
    .bracket(conn => conn.open() *> IO.sleep(Int.MaxValue.seconds))(conn => conn.close().void)

  val bracketProgram = for {
    fib <- bracketFetchUrl.start
    _   <- IO.sleep(1.second) *> fib.cancel
  } yield ()

  /** Exercise: read the file with the bracket pattern
    *   - open a scanner
    *   - read the file line by line, every 100 millis
    *   - close the scanner
    *   - if cancelled/throws error, close the scanner
    */
  def openFileScanner(path: String): IO[Scanner] =
    IO(new Scanner(new FileReader(new File(path))))

  def readLineByLine(scanner: Scanner): IO[Unit] =
    if (scanner.hasNextLine) IO(scanner.nextLine()).debug0 >> IO.sleep(100.millis) >> readLineByLine(scanner)
    else IO.unit

  def bracketReadFile(path: String): IO[Unit] =
    IO(s"opening file at $path") >>
      openFileScanner(path).bracket { scanner =>
        readLineByLine(scanner)
      } { scanner =>
        IO(s"closing file at $path").debug0 >> IO(scanner.close())
      }

  /** *************** Resources
    */
  def connFromConfig(path: String): IO[Unit] =
    openFileScanner(path)
      .bracket { scanner =>
        // acquire a connection based on the file
        IO(new Connection(scanner.nextLine())).bracket { conn =>
          conn.open() >> IO.never
        }(conn => conn.close().void)
      }(scanner => IO("closing file").debug0 >> IO(scanner.close()))
  // nesting resources are tedious

  val connectionResource = Resource.make(IO(new Connection("rockthejvm.com")))(conn => conn.close().void)
  // ... at a later part of your code

  val resourceFetchUrl = for {
    fib <- connectionResource.use(conn => conn.open() >> IO.never).start
    _   <- IO.sleep(1.second) >> fib.cancel
  } yield ()

  // resources are equivalent to brackets
  val simpleResource = IO("some resource")
  val usingResource: String => IO[String] = string => IO(s"using the string: $string").debug0
  val releaseResource: String => IO[Unit] = string => IO(s"finalizing the string: $string").debug0.void

  val usingResourceWithBracket = simpleResource.bracket(usingResource)(releaseResource)
  val usingResourceWithResource = Resource.make(simpleResource)(releaseResource).use(usingResource)

  /** Exercise: read a text file with one line every 100 millis, using Resource (refactor the bracket exercise to use
    * Resource)
    */
  def getResourceFromFile(path: String) = Resource.make(openFileScanner(path)) { scanner =>
    IO(s"closing file at $path").debug0 >> IO(scanner.close())
  }

  def resourceReadFile(path: String) =
    IO(s"opening file at $path") >>
      getResourceFromFile(path).use { scanner =>
        readLineByLine(scanner)
      }

  def cancelReadFile(path: String) = for {
    fib <- resourceReadFile(path).start
    _   <- IO.sleep(2.seconds) >> fib.cancel
  } yield ()

  // nested resources
  def connFromConfResource(path: String) =
    Resource
      .make(IO("opening file").debug0 >> openFileScanner(path))(scanner => IO("closing file").debug0 >> IO(scanner.close()))
      .flatMap(scanner => Resource.make(IO(new Connection(scanner.nextLine())))(conn => conn.close().void))

  // equivalent
  def connFromConfResourceClean(path: String) = for {
    scanner <- Resource.make(IO("opening file").debug0 >> openFileScanner(path))(scanner => IO("closing file").debug0 >> IO(scanner.close()))
    conn    <- Resource.make(IO(new Connection(scanner.nextLine())))(conn => conn.close().void)
  } yield conn

  val openConnection =
    connFromConfResourceClean("cats-effect/src/main/resources/connection.txt").use(conn => conn.open() >> IO.never)
  val canceledConnection = for {
    fib <- openConnection.start
    _   <- IO.sleep(1.second) >> IO("cancelling!").debug0 >> fib.cancel
  } yield ()

  // connection + file will close automatically

  // finalizers to regular IOs
  val ioWithFinalizer = IO("some resource").debug0.guarantee(IO("freeing resource").debug0.void)
  val ioWithFinalizer_v2 = IO("some resource").debug0.guaranteeCase {
    case Succeeded(fa)  => fa.flatMap(result => IO(s"releasing resource: $result").debug0).void
    case Errored(e @ _) => IO("nothing to release").debug0.void
    case Canceled()     => IO("resource got canceled, releasing what's left").debug0.void
  }

  override def run = ioWithFinalizer.void
}
