package ce2.d08_testing

import cats.effect.ContextShift
import cats.effect.IO
import cats.effect.Timer
import cats.effect.laws.util.TestContext
import munit.Assertions.assertEquals

import scala.concurrent.TimeoutException
import scala.concurrent.duration.DurationInt
import scala.util.Failure

object A2_MockingTimer extends App {

  val ctx = TestContext()

  implicit val cs: ContextShift[IO] = ctx.ioContextShift
  implicit val timer: Timer[IO] = ctx.timer

  val timeoutError = new TimeoutException
  val timeout = IO.sleep(10.seconds) *> IO.raiseError[Int](timeoutError)
  val f = timeout.unsafeToFuture()

  ctx.tick(5.seconds)
  assertEquals(f.value, None)

  ctx.tick(5.seconds)
  assertEquals(f.value, Some(Failure(timeoutError)))
}
