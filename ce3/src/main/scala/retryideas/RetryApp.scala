package retryideas

import cats.effect.IO
import cats.effect.IOApp
import cats.implicits.catsSyntaxApplicativeId
import retry.RetryDetails
import retry.RetryPolicy
import retry.implicits.retrySyntaxError
import scala.concurrent.duration.DurationInt

object RetryApp extends IOApp.Simple {

  // mutable state to decide whether to produce the value / fail with error
  var x: Int = 0

  def make: IO[Int] =
    IO.println(s"$x") >> {
      x match {
        case x if x < 10 => IO.raiseError(new IllegalStateException(s"failed x=$x"))
        case x           => (x * 100).pure[IO]
      }
    }

  val policy: RetryPolicy[IO] = {
    import retry.RetryPolicies._
    // 6 retries starting from 1 gives us +1 +2 +4 +8 +16 +32 = 63 sec ~= 1 min
    val growing = limitRetries[IO](5) join exponentialBackoff[IO](1.second)
    // constant never terminating retry
    val constant = constantDelay[IO](10.seconds)

    growing followedBy constant
  }

  val onError = (t: Throwable, d: RetryDetails) =>
    IO { x = x + 1; println(s"error caught. ${System.currentTimeMillis() % 100000} retrying...") }

  override def run: IO[Unit] =
    make
      .retryingOnAllErrors(policy, onError)
      .flatTap(n => IO.println(s"recovered after $x retries, got value: $n"))
      .flatTap(_ => IO { x = 0; println("cleared") })
      .flatTap(_ => IO.println(s"x = $x"))
      .void

}
