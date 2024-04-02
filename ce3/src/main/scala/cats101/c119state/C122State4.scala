package cats101.c119state

import cats.data.State
import cats.data.State.{get, inspect, modify, set}

object C122State4 extends App {

  val program: State[Int, (Int, Int, Int, Int)] = for {
    a <- get[Int] // given          a=100
    _ <- set[Int](a + 1) // +1 via set
    b <- get[Int] //                b=101
    _ <- modify[Int](_ + 1) // _+1 via modify
    d <- get[Int] // _+1            d=102
    c <- inspect[Int, Int](_ * 1000) //        c=102000
  } yield (a, b, c, d)

  println(program.run(100).value)

}
