package el_meter

import cats.effect.IO
import cats.implicits._
import munit.CatsEffectSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.DurationInt

class EmptyChunkExploreMunit extends CatsEffectSuite with Matchers {

  test("EmptyChunk explore") {

    fs2.Stream
      .awakeEvery[IO](5.seconds)
      .groupWithin(10, 1.second)
      .evalMap(x => IO.println(s"got ${x.head.get.toSeconds}"))
      .compile
      .drain

  }

  test("simple - scalatest") {
    IO(1)
      .map(_ shouldBe 1)
  }

  test("simple - munit") {
    IO(1)
      .map(assertEquals(_, 1))
  }

}
