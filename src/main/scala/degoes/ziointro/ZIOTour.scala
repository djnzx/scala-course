package degoes.ziointro

import degoes.ziointro.Z4Loop.repeat
import zio._
import zio.console.putStrLn

object Z1HelloWord extends App {
  import zio.console._

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    putStrLn("Hello") as 0
  }
}

object Z2Numbers extends App {
  import zio.console._

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    putStrLn("Hello,") *> putStrLn("World!") *> ZIO.succeed(0)
  }
}

object Z3Fail extends App {

  import zio.console._

  val failed = putStrLn("About to fail...") *>
    ZIO.fail("O-o..") *>
    putStrLn("Will never be printed!")

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    //    (failed as 0) orElse ZIO.succeed(1)
    //    failed.fold(_ => 1, _ => 0)
    (failed as 0) catchAllCause (cause => putStrLn(s"cause: ${cause.prettyPrint}") as 1)
  }
}

object Z4Loop extends App {
  import zio.console._

  def repeat[R, E, A](n: Int)(effect: ZIO[R, E, A]): ZIO[R, E, A] =
    if (n <= 1) effect
    else effect *> repeat(n - 1)(effect)

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] = {
    repeat(1_000_000)(putStrLn("hello")) as 0
  }
}

object Z5Interact extends App {
  import zio.console._

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    (for {
      _    <- putStrLn("what is your name")
      name <- getStrLn
      _    <- putStrLn(s"Hello, ${name}")
    } yield 0) orElse ZIO.succeed(1)

}

object Z6NumberGuesser extends App {
  import zio.console._
  import zio.random._

  def analyze(num: Int, guess: String): ZIO[Console, Nothing, Unit] =
    if (num.toString == guess.trim) putStrLn("Correct!")
    else putStrLn(s"You didn't! was: $num")

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    (for {
      random <- nextInt(3)
      _      <- putStrLn("Please guess")
      guess  <- getStrLn
      _      <- analyze(random, guess)
    } yield 0) orElse ZIO.succeed(1)
}

object AlarmImproved extends App {
  import zio.console._
  import zio.duration._
  import java.io.IOException
  import java.util.concurrent.TimeUnit

  def toDouble(s: String): Either[NumberFormatException, Double] =
    try Right(s.toDouble) catch { case e: NumberFormatException => Left(e) }

  lazy val getAlarmDuration: ZIO[Console, IOException, Duration] = {
    def parseDuration(input: String): Either[NumberFormatException, Duration] =
      toDouble(input).map(d => Duration((d * 1000.0).toLong, TimeUnit.MILLISECONDS))

    val fallback = putStrLn("Wrong number!") *> getAlarmDuration

    for {
      _        <- putStrLn("Enter number of seconds to sleep:")
      input    <- getStrLn
      duration <- ZIO.fromEither(parseDuration(input)) orElse fallback
    } yield duration
  }

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    (for {
      duration <- getAlarmDuration
      fiber     <- (putStr(".") *> ZIO.sleep(1.second)).forever.fork
      _        <- ZIO.sleep(duration)
      _        <- putStrLn("Woke Up!")
      _        <- fiber.interrupt

    } yield 0) orElse ZIO.succeed(1)
}

object ComputePi extends App {
  import zio.console._
  import zio.duration._
  import zio.random._
  import zio.clock._
  import zio.stm._

  final case class PiState(inside: Long, total: Long)

  def estimatePi(inside: Long, total: Long): Double = (inside.toDouble / total.toDouble) * 4.0

  def insideCircle(x: Double, y: Double): Boolean = Math.sqrt(x * x + y * y) <= 1.0

  val randomPoint: ZIO[Random, Nothing, (Double, Double)] = nextDouble zip nextDouble

  def updateOnce(ref: Ref[PiState]): ZIO[Random, NoSuchElementException, Unit] =
    for {
      point  <- randomPoint
      (x, y) = point
      inside = if (insideCircle(x,y)) 1 else 0
      _      <- ref.update(st => PiState(st.inside + inside, st.total + 1))
    } yield ()

  def printEstimate(ref: Ref[PiState]): ZIO[Console, Nothing, Unit] =
    for {
      st <- ref.get
      _  <- putStrLn(s"${estimatePi(st.inside, st.total)}")
    } yield ()

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    (for {
      ref      <- Ref.make(PiState(0L, 0L))
      worker   = updateOnce(ref).forever
      workers  = List.fill(4)(worker)
      fiber1    <- ZIO.forkAll(workers)
      fiber2    <- (printEstimate(ref) *> ZIO.sleep(1.second)).forever.fork
      _        <- putStrLn("any key to terminate")
      _        <- getStrLn *> (fiber1 zip fiber2).interrupt
    } yield 0) orElse ZIO.succeed(1)
}

object StmDiningPhilosophers extends App {
  import zio.console._
  import zio.duration._
  import zio.random._
  import zio.clock._
  import zio.stm._




  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = ???
}






