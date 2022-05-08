package _playground

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource

object ResourceApp extends IOApp.Simple {

  val r1r = Resource.make(IO { println("obtaining 1"); 1 })(_ => IO(println("releasing 1")))
  val r2r = Resource.make(IO { println("obtaining 2"); 2 })(_ => IO(println("releasing 2")))

  def add(a: Int, b: Int) = IO { println("adding"); a + b }

  val resource: Resource[IO, IO[Int]] = for {
    r1 <- r1r
    r2 <- r2r
  } yield add(r1, r2)

  override def run: IO[Unit] = resource.use(identity).void
}
