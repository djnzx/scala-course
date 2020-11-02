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

  def multiply(r: Res) = r.x * 10
  def mult2(x: Int, y: Int) = x * y
  val mult: Res => Int = multiply
  val mult2f: (Int, Int) => Int = mult2
  val mult2t = mult2f.tupled

  /**
    * I can create a function
    */
  val zf1: ZIO[Res,        Nothing, Int] = ZIO.fromFunction(multiply)
  val zf2: ZIO[Res,        Nothing, Int] = ZIO.fromFunction(mult)
  val zf3: ZIO[(Int, Int), Nothing, Int] = ZIO.fromFunction(mult2t)
  
  val zr1: ZIO[Res, Nothing, Nothing] => IO[Nothing, Nothing] = ZIO.provide(r1)
  val zr2: ZIO[Res, Nothing, Nothing] => IO[Nothing, Nothing] = ZIO.provide(r2)

  

  val done = ZIO.effect(println("Done!"))

  override def run(args: List[String]): URIO[ZEnv, ExitCode] =
    done.exitCode

}
