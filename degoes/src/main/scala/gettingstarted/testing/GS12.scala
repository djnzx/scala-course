package gettingstarted.testing

/**
  * https://www.youtube.com/watch?v=6A1SA5Be9qw
  */
import zio._
import zio.clock._
import zio.console._
import zio.duration._
import zio.test.Assertion._
import zio.test._
import zio.test.environment._

/** type inference */
object GS12Code {
  /**
    * `Console with Clock` inferred automatically
    */
  val alarm: ZIO[Console with Clock, Throwable, Unit] = for {
    _    <- console.putStr("Enter time to sleep (in seconds):")
    time <- console.getStrLn.mapEffect(_.toInt)
    _    <- clock.sleep(time seconds)
    _    <- console.putStrLn("Time gone!")
    _    <- console.putStrLn("Wake Up!")
  } yield ()
}

object GS12App extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    GS12Code.alarm.orDie.as(ExitCode.success)
} 

/** doesn't work */
object GS12Spec extends DefaultRunnableSpec {
  def spec: ZSpec[TestEnvironment, Any] = suite("GS12")(
    testM("works correctly"){
      for {
        _      <- TestConsole.feedLines("5")
        _      <- GS12Code.alarm
        _      <- TestClock.adjust(5.seconds)
        output <- TestConsole.output
        ass: TestResult = assert(output)(equalTo(Vector("Time gone!\n", "Wake Up!\n")))
      } yield ass
    }
  )
}
