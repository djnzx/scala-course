package chi

object Seven extends App {

  val r = (1 to 1_000).foldLeft(1)((a, _) => (a * 7) % 10)
  println(r)
  val delta = 1000 % 3
  println(delta)

}
