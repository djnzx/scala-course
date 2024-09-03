package el_meter

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.duration.DurationInt

class EmptyChunkExplore extends AnyFunSuite {

  test("EmptyChunk explore") {

    fs2.Stream
      .awakeEvery[IO](5.seconds)
      .groupWithin(10, 1.second)
      .evalMap(x => IO.println(s"got ${x.head.get.toSeconds}"))
      .compile
      .drain
      .unsafeRunSync()

  }

}
