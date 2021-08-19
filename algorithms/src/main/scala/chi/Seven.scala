package chi

object Seven extends App {

  val r = (1 to 1_000_000_000).foldLeft(1)((a, _) => (a * 7) % 10)
  println(r)

}
