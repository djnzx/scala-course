package eff

import cats.effect.IO
import cats.implicits._

object Lifting {

  // pure data
  val userOpt: Option[String] = Some("Jim")
  val userOpt2: Right[Throwable, String] = Right("Jim")

  // lifting syntax 1
  val u1: IO[String] = IO.fromOption(userOpt)(new IllegalArgumentException("should be Some"))
  val u2: IO[String] = IO.fromEither(userOpt2)

  // lifting syntax 2
  val u3: IO[String] = userOpt.liftTo[IO](new IllegalArgumentException("should be Some"))
  val u4: IO[String] = userOpt2.liftTo[IO]


}
