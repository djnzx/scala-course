package cats.monad_trans

import cats.data.EitherT
import cats.instances.list._

object App4EitherT extends App {
  val data: List[Either[String, Int]] = List(Right(1), Right(2), Right(3), Left("E1"), Left("E2"))
  val datat: EitherT[List, String, Int] = EitherT(data) // EitherT[List, String, Int]

  val data2t: EitherT[List, String, Int] = for {
    x <- datat // we do conventional flatMap as if this is not Either[String, Int] but just List[Int]
    y = x + 1  //
  } yield y

  val data3 = data2t.value
  println(data)
  println(datat)
  println(data2t)
  println(data3)

}
