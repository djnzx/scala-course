package cequeue

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.std.Queue
import scala.concurrent.duration.DurationInt

object LearningCEQueue1 extends IOApp.Simple {

  /** Queue is "mutable" like Ref
    * based on the same CAS primitive
    *
    * take: F[A] - gets element, and block until element is appeared
    * tryTake: F[Option[A]] - non-blocking, get / or None
    * tryTake(n: Int): F[List[A]] - non-blocking, get at most `n` elements or less
    */
  val empty: IO[Queue[IO, Int]] = Queue.unbounded

  def printSize(q: Queue[IO, Int]) = q.size.flatMap(size => IO.println(s"size: $size"))

  def add3get4(q: Queue[IO, Int]) = for {
    _ <- printSize(q)

    _ <- q.offer(70)
    _ <- printSize(q)

    _ <- q.offer(80)
    _ <- printSize(q)

    _ <- q.offer(90)
    _ <- printSize(q)

    x1  <- q.take
    _   <- IO.println(x1)
    x2  <- q.take
    _   <- IO.println(x2)
    x3  <- q.take
    _   <- IO.println(x3)
    x4  <- q.take               // block until element appears -
    _   <- IO.println(x4)
    x4o <- q.tryTake            // non-blocking, returns None / Some(x)
    _   <- IO.println(x4o)
    l   <- q.tryTakeN(Some(10)) // non-blocking
    _   <- IO.println(l)
    _   <- printSize(q)
  } yield ()

  val sleep10 = IO.sleep(10.seconds)

  def add1(q: Queue[IO, Int]) = for {
    _ <- q.offer(100)
  } yield ()

  override def run: IO[Unit] = for {
    q  <- empty
    f1 <- add3get4(q).start
    f2 <- (sleep10 >> add1(q)).start
    _ <- IO.println("started sleeping")
    _  <- f1.join
    _  <- f2.join
    _ <- IO.println("finished sleeping")
  } yield ()

}
