package _for

object ForApp extends App {
  val x: Seq[String] = for {
    i <- 1 to 4
    j <- 1 to 4
    if j >= i
  } yield s"<$i:$j>"
  println(x)
}
