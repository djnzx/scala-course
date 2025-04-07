package retryexplore

import RetryPlayground._
import cats.effect._
import cats.implicits._
import munit.CatsEffectSuite
import retry.RetryDetails
import retry.RetryDetails.GivingUp
import retry.RetryPolicies._
import retry.implicits._
import tools.LogSyntax
import scala.concurrent.duration.DurationInt

object RetryPlayground {
  case class ServiceError(code: Int) extends Throwable
}

class RetryPlayground[F[_]](implicit F: Async[F]) {

  val retryPolicy = constantDelay[F](10.millis) join limitRetries[F](2)

  // 1 2 4 8 16 32 64...
  val exponentialInf = exponentialBackoff[F](1.second)

  // 1 2 4 8 16
  def exponential5 = exponentialInf join limitRetries[F](5)

  // 1 2 4 8 16 10 10...
  val exponential5constantInf = exponential5 followedBy constantDelay(10.seconds)

  // 1 2 4 8 16 10 10 10 10 10
  val retryPolicy2 = exponential5constantInf join limitRetries[F](10)

  def logF(s: String) =
    F.delay(println(s))

  def random(): F[Double] =
    F.delay(scala.util.Random.nextDouble())

  def trivial(): F[Int] =
    42.pure[F]

  def alwaysFail(): F[Int] =
    ServiceError(409).raiseError[F, Int]

  def failsSporadically(x: Double): F[Int] = x match {
    case x if x < 10 => ServiceError(409).raiseError[F, Int]
    case x           => x.toInt.pure[F]
  }

  // we can't modify this code,
  // but it fails in 0.0001% cases, for example inaccessibility third party service, or some I/O error, and:
  // 1. we need to handle it
  // 2. we need to write a unit test for handling
  def hiddenTreasure(): F[Int] =
    random()
      .flatMap {
        case x if x < 0.001 => ServiceError(403).raiseError[F, Int]
        case x              => (x * 100).toInt.pure[F]
      }

  def isWorthRetrying(t: Throwable): F[Boolean] = t match {
    case ServiceError(code) if code >= 500 => true.pure[F]
    case _                                 => false.pure[F]
  }

  def onError(t: Throwable, rd: RetryDetails): F[Unit] = rd match {
    case GivingUp(_, _) => logF("R.Giving Up") >> t.raiseError[F, Unit]
    case _              => logF("R.Retrying...")
  }

  implicit class RetryOps[A](fa: F[A]) {
    def retryTwiceThenFail: F[A] =
      fa.retryingOnSomeErrors(
        isWorthRetrying,
        retryPolicy,
        onError
      )
  }

  def hiddenTreasureHandled() =
    hiddenTreasure()
      .retryTwiceThenFail

  // we need flaky function in a controllable way
  def flaky(state: Ref[F, Int]): F[String] =
    state.modify {
      case 0 => (0, logF("f.succeed") >> "Ok".pure[F])
      case n => (n - 1, logF("f.failure") >> ServiceError(511).raiseError[F, String])
    }.flatten

  def flakyButShouldNotBeHandled(state: Ref[F, Int]): F[String] =
    state.modify {
      case 0 => (0, "Ok".pure[F])
      case n => (n - 1, ServiceError(400).raiseError[F, String])
    }.flatten

}

class RetryPlaygroundTest extends CatsEffectSuite with LogSyntax {

  val sv = new RetryPlayground[IO]
  import sv.RetryOps

  test("trivial") {
    sv.trivial()
      .assert(_ == 42)
  }

  test("capture errors") {
    sv.alwaysFail()
      .attempt
      .assert(_ == ServiceError(409).asLeft)
  }

  test("sporadically - result") {
    sv.failsSporadically(11)
      .assert(_ == 11)
  }

  test("sporadically - failure") {
    sv.failsSporadically(5)
      .attempt
      .assert(_ == ServiceError(409).asLeft)
  }

  test("flaky-controlled - succeed - no retries") {
    Ref[IO].of(0)
      .flatMap(st => sv.flaky(st).retryTwiceThenFail)
      .assert(_ == "Ok")
  }

  test("flaky-controlled - succeed - 1 retry") {
    Ref[IO].of(1)
      .flatMap(st => sv.flaky(st).retryTwiceThenFail)
      .assert(_ == "Ok")
  }

  test("flaky-controlled - succeed - 2 retry") {
    Ref[IO].of(2)
      .flatMap(st => sv.flaky(st).retryTwiceThenFail)
      .assert(_ == "Ok")
  }

  test("flaky-controlled - failure - only 2 of 3 retries made") {
    Ref[IO].of(3)
      .flatMap(st => sv.flaky(st).retryTwiceThenFail)
      .attempt
      .assert(_ == ServiceError(511).asLeft)
  }

  test("flaky-controlled - failure - shouldn't be retried") {
    Ref[IO].of(5)
      .flatMap(state => sv.flakyButShouldNotBeHandled(state).retryTwiceThenFail)
      .attempt
      .assert(_ == ServiceError(400).asLeft)
  }

}
