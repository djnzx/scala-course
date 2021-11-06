package catsx.c113writer

import cats.data.Writer

object C109Writer2 extends App {

  type LS = List[String]

  val w0 = Writer.tell(List("Line0"))
  val w1 = Writer(List("Line1"), 1)
  val w2 = Writer(List("Line2"), 2)
  val w5 = Writer(List("Line3"), 5)
  val w6 = Writer.value[LS, Int](10)

  /**
    * flatMap applies the function and combines the log
    * map maps the value
    */
  val combination = for {
    _ <- w0
    a <- w1
    b <- w2
    c <- w5
    d <- w6
  } yield a + b + c + d

  pprint.pprintln(combination.run)

}
