package log4c

import cats.MonadThrow
import cats.effect.unsafe.IORuntime
import cats.effect.{IO, Resource}
import cats.syntax.all._
import munit.diff.Printer
import munit.internal.console.Printers
import munit.{CatsEffectSuite, TestOptions}
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}
import org.typelevel.log4cats.testing.StructuredTestingLogger
import org.typelevel.log4cats.testing.StructuredTestingLogger.{DEBUG, ERROR, INFO, TRACE, WARN}

import java.io.{PrintWriter, StringWriter}

// https://github.com/typelevel/log4cats/blob/main/testing/README.md
class OriginalExampleTest extends CatsEffectSuite {
  private val loggerFixture = ResourceFunFixture[LoggingHelper]((options: TestOptions) =>
    Resource.make(IO.delay {
      val logger = StructuredTestingLogger.impl[IO]()
      LoggingHelper(logger, logger.addContext(Map("TestName" -> options.name)))
    })(_ => IO.unit)
  )

  private val loggerPrinter = new Printer {
    override def print(value: Any, out: StringBuilder, indent: Int): Boolean =
      value match {
        case loggingHelper: LoggingHelper =>
          out.appendAll("Logs:")
          val indentation = " " * (indent + 2)
          val innerIndentation = " " * (indent + 8)
          loggingHelper.logged.foreach { log =>
            out
              .append('\n')
              .append(indentation)
              .appendAll(log match {
                case _: TRACE => "TRACE "
                case _: DEBUG => "DEBUG "
                case _: INFO => "INFO  "
                case _: WARN => "WARN  "
                case _: ERROR => "ERROR "
              })
              .appendAll(log.message)

            log.ctx.foreach {
              case (k, v) => out.append('\n').appendAll(innerIndentation).appendAll(k).appendAll(" -> ").appendAll(v)
            }

            log.throwOpt.foreach { throwable =>
              val stringWriter = new StringWriter
              throwable.printStackTrace(new PrintWriter(stringWriter))
              stringWriter.toString.split('\n').foreach { line =>
                out.append('\n').appendAll(innerIndentation).appendAll(line)
              }
            }
          }
          true

        case _ => false
      }
  }

  override def munitPrint(clue: => Any): String =
    clue match {
      case message: String => message
      case value => Printers.print(value, loggerPrinter)
    }

  loggerFixture.test("avoid logging for successful tests") { loggingHelper =>
    import loggingHelper.loggerFactory
    CodeUnderTest.logAndDouble[IO]("5").assertEquals(10, loggingHelper)
  }

  loggerFixture.test("output logging for failing tests") { loggingHelper =>
    import loggingHelper.loggerFactory
    CodeUnderTest.logAndDouble[IO]("5.").assertEquals(10, loggingHelper)
  }
}

/**
 * Simple class to reduce some of the boilerplate, and fix the type argument of
 * `StructuredTestingLogger` and avoid an unchecked type cast in `loggerPrinter`
 */
final case class LoggingHelper(underlyingLogger: StructuredTestingLogger[IO],
                               loggerWithContext: SelfAwareStructuredLogger[IO]) {
  implicit val loggerFactory: LoggerFactory[IO] = new LoggerFactory[IO] {
    override def getLoggerFromName(name: String): SelfAwareStructuredLogger[IO] = loggerWithContext

    override def fromName(name: String): IO[SelfAwareStructuredLogger[IO]] = loggerWithContext.pure[IO]
  }

  def logged(implicit runtime: IORuntime): Vector[StructuredTestingLogger.LogMessage] =
    underlyingLogger.logged.unsafeRunSync()
}

object CodeUnderTest {
  def logAndDouble[F[_]: LoggerFactory : MonadThrow](input: String): F[Int] = {
    val logger = LoggerFactory[F].getLogger
    for {
      _ <- logger.info(s"Input $input")
      intVal <- MonadThrow[F].catchNonFatal(input.toInt).recoverWith {
        case ex: NumberFormatException => logger.warn(ex)("Recovering to 0").as(0)
      }
      doubled = intVal * 2
      _ <- logger.info(s"Result: $doubled")
    } yield doubled
  }
}