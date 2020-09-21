package ninetynine

object P22 {
  def range(min: Int, max: Int): List[Int] = min to max toList

  def test(): Unit = {
    val r = range(4,9)
    println(r)
  }
}
