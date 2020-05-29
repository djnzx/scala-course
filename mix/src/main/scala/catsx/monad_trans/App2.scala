package catsx.monad_trans

import cats.data.OptionT
import cats.Monad
import cats.instances.list._     // for Monad
import cats.syntax.applicative._ // for pure

object App2 extends App {
  type ListOption[A] = OptionT[List, A]

  val data = List(Option(10), Option(20), Option(30))

  val result2: ListOption[Int] = OptionT(data)
  val result3: ListOption[Int] = 2.pure[ListOption]

  val result1: OptionT[List, Int] = OptionT(data)
  val result4: ListOption[List[Int]] = List(10,20,30).pure[ListOption]

  val r4: OptionT[List, Int] =
    result2.flatMap((x: Int) =>
      result3.map((y: Int) =>
        x + y
      )
    )

  val r4unpacked: List[Option[Int]] = r4.value;
  println(r4unpacked)





}
