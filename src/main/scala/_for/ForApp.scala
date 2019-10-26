package _for

object ForApp extends App {

  val range: Seq[Int] = 1 to 4 // Range.Inclusive
  val range1: Seq[Int] = range.flatMap(x => List(x))
  val range2: Seq[Int] = for {
    i <- range
  } yield i * 2

  println
  println(range)
  println(range1)
  println(range2)

}
