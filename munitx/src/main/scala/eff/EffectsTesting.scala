package eff

import cats.effect._
import munit.CatsEffectSuite
import skunkx.Tools

class EffectsTesting extends CatsEffectSuite with Tools {

  val app = new UsingErrorChannelExample[IO]

  test("0") {
    IO.realTime
      .map(_.toMillis)
      .log
  }

  test("1") {
    app.block1
      .assert(_ == "3")
  }

  test("2") {
    app.block2
      .assert(_ == "handled E2(boom!)")
  }

  test("3") {
    app.block3
      .assert(_ == "handled E1(999)")
  }

}
