package aa_fp

object Fps001 extends App {

  val ls = List(1,2,3,4)

  def sum1(xs: List[Int]): Int = {
    var sum = 0
    for (el <- xs) {
      sum += el
    }
    sum
  }

  def sum2(xs: List[Int]): Int = xs match {
    case Nil => 0
    case head::tail => head + sum2(tail)
  }

  def sum3(xs: List[Int]): Int = sum3r(xs, 0)
  def sum3r(xs: List[Int], acc: Int): Int = xs match {
    case Nil => acc
    case head::tail => sum3r(tail, acc + head)
  }

  println(sum1(ls))
  println(sum1(ls))
  println(sum3(ls))

}
