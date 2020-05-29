package catsx

import cats.data.OptionT
import cats.instances.list._ // instance for Applicative[List[A]]
import cats.syntax.applicative._ // pure

object C131MonadTransform extends App {

  type ListOption[A] = OptionT[List, A]

  val r1: ListOption[Int] = OptionT(List(Option(10), Option(20), None, Option(30)))
//  val r2: ListOption[Int] = 5.pure[ListOption]
  val r2: ListOption[Int] = OptionT(List(Option(1), Option(2)))

  val z: OptionT[List, Int] = for {
    x <- r1
    y <- r2
  } yield x + y

  println(z)
}
