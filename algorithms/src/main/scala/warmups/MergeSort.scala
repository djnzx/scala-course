package warmups

object MergeSort {

  def ri = (scala.math.random() * 100).toInt

  def gen(n: Int) = LazyList.unfold(n) {
    case 0 => None
    case n => Some(ri, n - 1)
  }

  def merge(as: List[Int], bs: List[Int], r: List[Int] = Nil): List[Int] = (as, bs) match {
    case (Nil, Nil)                     => r.reverse
    case (Nil, bs)                      => (bs.reverse ::: r).reverse
    case (as, Nil)                      => (as.reverse ::: r).reverse
    case (ah :: at, bh :: _) if ah < bh => merge(at, bs, ah :: r)
    case (_, bh :: bt)                  => merge(as, bt, bh :: r)
  }

  def sort(xs: Array[Int]): Iterable[Int] = {

    def go(xs: Array[Int], l: Int, r: Int): Iterable[Int] = ???
//    {
//      if (l < r) {
//        val m = (l + r) / 2
//        val p1 = go(xs, l, m).toList
//        val p2 = go(xs, m + 1, r).toList
//        merge(p1, p2)
//      }
//
//    }

    go(xs, 0, xs.length)
  }

}

object MergeSortApp extends App {

  import MergeSort._

  val l1 = gen(10).sorted.toList
  val l2 = gen(10).sorted.toList
  val l3 = merge(l1, l2)
  pprint.pprintln(l1)
  pprint.pprintln(l2)
  pprint.pprintln(l3)

}
