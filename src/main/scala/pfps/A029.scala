package pfps

import cats.effect.concurrent.Ref
import cats.effect.{IO, Sync}
import cats.implicits._

object A029 extends App {

  trait Counter[F[_]] {
    def incr: F[Unit]
    def get: F[Int]
  }

  // state shall not leak
  class LiveCounter[F[_]] private (ref: Ref[F, Int]) extends Counter[F] {
    def incr: F[Unit] = ref.update(_ + 1)
    def get: F[Int] = ref.get
  }
  object LiveCounter {
    def make[F[_]: Sync]: F[Counter[F]] =
      Ref.of[F, Int](0).map(new LiveCounter(_))
  }
  // It is also a good practice to return our new interface wrapped in F
  // since its creation is effectful (allocates a mutable reference)
  // and because the counter itself is also mutable state.
//  val c = LiveCounter.make

  object LiveCounterAnon {
    def make[F[_]: Sync]: F[Counter[F]] =
      Ref.of[F, Int](0).map(ref =>
      new Counter[F] {
        override def incr: F[Unit] = ref.update(_ + 1)
        override def get: F[Int] = ref.get
      })
  }

  def program(counter: Counter[IO]): IO[Unit] = counter.incr *> ???


}
