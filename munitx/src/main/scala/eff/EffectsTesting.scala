package eff

import cats.effect._
import munit.CatsEffectSuite
import tools.LogSyntax

class EffectsTesting extends CatsEffectSuite with LogSyntax {

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

  test("4") {
    app.block4
      .assert(_ == "-13")
  }

  test("5") {
    app.block5
      .assert(_ == "handled E1(999)")
  }

}
