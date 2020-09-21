package ninetynine

object P25 {
  def permutate[A](xs: List[A]): List[A] = P24.takeNrnd(xs, Nil, xs.length)._1

  def test(): Unit = {
    val permutation = permutate(P22.range(1, 30))
    println(permutation)
  }

}
