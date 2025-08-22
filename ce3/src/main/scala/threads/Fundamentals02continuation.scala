package threads

import cats.effect.{IO, Spawn}
import munit.CatsEffectSuite
import org.scalatest.matchers.should.Matchers

class Fundamentals02continuation extends CatsEffectSuite with Tools with Matchers {

  test("IO.delay(x) continue on the started") {
    IO.delay(tn)
      .flatTap(printlnF)
      .map(_ => tn)
      .flatTap(printlnF)
      .map(_ => tn)
      .flatTap(printlnF)
  }

  test("IO.blocking(x) continue on the started") {
    IO.blocking(tn)
      .flatTap(printlnF)
      .map(_ => tn)
      .flatTap(printlnF)
      .map(_ => tn)
      .flatTap(printlnF)
  }

  test("IO.blocking(x) cede") {
    IO.blocking(tn)
      .flatTap(printlnF)
      .flatTap(_ => Spawn[IO].cede) // shift back
      .map(_ => tn)
      .flatTap(printlnF)
      .map(_ => tn)
      .flatTap(printlnF)
  }

}
