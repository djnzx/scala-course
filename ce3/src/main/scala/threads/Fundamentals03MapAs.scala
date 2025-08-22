package threads

import cats.effect.IO
import munit.CatsEffectSuite
import org.scalatest.matchers.should.Matchers

class Fundamentals03MapAs extends CatsEffectSuite with Tools with Matchers {

  test("as") {
    IO.delay(tn)
      .flatTap(printlnF)
      .as(tn)
      .flatTap(printlnF)
      .as(tn)
      .flatTap(printlnF)
  }

  test("map") {
    IO.delay(tn)
      .flatTap(printlnF)
      .map(_ => tn)
      .flatTap(printlnF)
      .map(_ => tn)
      .flatTap(printlnF)
  }

  test("flatMap") {
    IO.delay(tn)
      .flatTap(printlnF)
      .flatMap(_ => tnF)
      .flatTap(printlnF)
      .flatMap(_ => tnF)
      .flatTap(printlnF)
  }

}
