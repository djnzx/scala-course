package catsx.monad_trans

import cats.data.OptionT
import cats.instances.list._     // for Monad
import cats.syntax.applicative._ // for pure

object App3OptionT extends App {

  // everything is cool if we have plain structure
  val data1: List[Int] = List(1,2,3)
  val delta = 1

  val data1m: List[Int] = for {
    x <- data1
    y = x + delta
  } yield y

  // let's wrap contents in other monad
  val data2: List[Option[Int]] = List(Some(1), Some(2), None, Some(3))

  val data2m: List[Option[Int]] = for {
    x <- data2
    y = x.map(_ + delta) // <- here is the problem we need to know how to deal with that monad
  } yield y

  println(data2)
  println(data2m)

  // let's represent it in another way
  // it has knowledge that List is type List[Option[Int]]
  val data3: OptionT[List, Int] = OptionT(data2)
  val data3m: OptionT[List, Int] = for {
    x <- data3     // flatMap and map of OptionT[List, A]
    y = x + delta  // work as if they were List[A]
  } yield y
  // let's unpack it:
  val data4 = data3m.value

  println(data2)
  println(data3)
  println(data3m)
  println(data4)
  val deltat: List[Int] = delta.pure[List]


}
