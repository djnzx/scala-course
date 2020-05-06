package catsx

import cats.data.WriterT
import cats.implicits._

object C113WriterT extends App {
  // 1. we have data
  val o_int: Option[Int] = Some(123)
  // 2. pretend our data[Int] as value for Writer[List[String], Int]
  // Option[Int] => Writer[List[String], Int] with knowledge Option[...]
  val writer1: WriterT[Option, List[String], Int] = WriterT.liftF[Option, List[String], Int](o_int)
  // 3. work with       WriterT[Option, List[String], Int]
  // as if we work with Writer [        List[String], Int]
  // but Option...
  val writer2: WriterT[Option, List[String], Int] = writer1
    .tell(List("a","b","c"))
    .tell(List("d","e","f"))
    .map(x => x + 1)
  val opt_w: Option[(List[String], Int)] = writer2.run
  println(opt_w)
}
