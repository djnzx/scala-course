package log4c

import cats.MonadThrow
import cats.effect.IO
import cats.syntax.all._
import munit.CatsEffectSuite
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.testing.StructuredTestingLogger

object MyCodeCanFailAndDoesLogging {
  // my code, which can fail and does logging
  def do_something[F[_]: LoggerFactory: MonadThrow](input: String): F[Int] = {
    val logger = LoggerFactory[F].getLogger
    for {
      _ <- logger.info(s"Input $input")
      x <- MonadThrow[F]
             .catchNonFatal(input.toInt)
             .recoverWith { case ex: NumberFormatException => logger.warn(ex)("Recovering to 0").as(0) }
      x2 = x * 2
      _ <- logger.info(s"Result: $x2")
    } yield x2
  }
}

class SilenceLogForNonFailedTests extends CatsEffectSuite {
  import MyCodeCanFailAndDoesLogging._

  private val testLogger = StructuredTestingLogger.impl[IO]()
  implicit val loggerFactory: LoggerFactory[IO] = new LoggerFactory[IO] {
    override def getLoggerFromName(name: String): SelfAwareStructuredLogger[IO] = testLogger
    override def fromName(name: String): IO[SelfAwareStructuredLogger[IO]] = testLogger.pure[IO]
  }

  test("1") {
    do_something[IO]("5")
      .assertEquals(10)
  }

  test("2") {
    do_something[IO]("5abc")
      .assertEquals(10)
  }

}
