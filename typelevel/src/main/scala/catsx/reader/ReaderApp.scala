package catsx.reader

import cats.data.Reader

object ReaderApp extends App {

  val inc1: Int => Int = _ + 1
  val inc2: Int => Int = _ + 2
  val inc3: Int => Int = _ + 3

  val rm1: Reader[Int, Int] = Reader(inc1)
  val rm2: Reader[Int, Int] = Reader(inc2)
  val rm3: Reader[Int, Int] = Reader(inc3)

  val rm: Reader[Int, Int] = for {
    r1 <- rm1
    r2 <- rm2
    r3 <- rm3
  } yield r1 + r2 + r3

  println(rm.map(_ + 10).run(1)) // 19
  println(rm.run(10)) // 36



}
