package nomicon.ch02

import zio._

object ZApp3 extends App {

  // passing dependencies
  trait Res {
    def x: Int
  }

  val r1: Res = new Res {
    override def x: Int = 1
  }

  val r2: Res = new Res {
    override def x: Int = 2
  }

  val zr1: ZIO[Res, Nothing, Nothing] => IO[Nothing, Nothing] = ZIO.provide(r1)

  val zr2: ZIO[Res, Nothing, Nothing] => IO[Nothing, Nothing] = ZIO.provide(r2)


  val done = ZIO.effect(println("Done!"))

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    done.exitCode

}
