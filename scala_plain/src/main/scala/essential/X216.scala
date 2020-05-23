package essential

object X216 extends App {
  val minOrdering: Ordering[Int] = Ordering.fromLessThan[Int](_ < _)
  val maxOrdering: Ordering[Int] = Ordering.fromLessThan[Int](_ > _)
  implicit val absOrdering: Ordering[Int] = Ordering.fromLessThan[Int]((v1, v2) => Math.abs(v1) < Math.abs(v2))

  val list = List(3, 4, 2)
  val l1 = list.sorted(minOrdering)
  val l2 = list.sorted(maxOrdering)
  val l3 = list.sorted // scala.math.Ordering[Int]: low to high

  def x(implicit ord: Ordering[Int]) = {
    println(ord)
  }
  x

  assert(List(-4, -1, 0, 2, 3).sorted(absOrdering) == List(0, -1, 2, 3, -4))
  assert(List(-4, -3, -2, -1).sorted == List(-1, -2, -3, -4))

}
