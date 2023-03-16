package cequeue

object PercentageApp extends App {

  def represent(x: Double) = {
    val level = (x * 10).toInt
    new String(Array.tabulate[Char](10) { idx => if (idx < level) 'O' else 'o' })
  }

  println(represent(0.05))
  println(represent(0.1))
  println(represent(0.2))
  println(represent(0.9))
  println(represent(0.95))
  println(represent(1))

}
