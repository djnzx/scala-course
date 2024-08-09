package eff

import cats.effect._
import munit.CatsEffectSuite
import skunkx.Tools

class EffectsTesting extends CatsEffectSuite with Tools {

  test("1") {
    IO.realTime
      .map(_.toMillis)
      .log
  }

}
