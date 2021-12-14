package fps

object Fps010Recursion1 extends App {

  val list: List[Int] = List(10, 20, 30)

  // recursive but non TR
  def sum[A: Numeric](list: List[A]): A = list match {
    case Nil    => implicitly[Numeric[A]].zero
    case h :: t => implicitly[Numeric[A]].plus(h, sum(t))
  }

  println(sum(list))

  def sum2[A: Numeric](list: List[A]): A = sumr(list, implicitly[Numeric[A]].zero)
  def sumr[A: Numeric](list: List[A], acc: A): A = list match {
    case Nil    => acc
    case h :: t => sumr(t, implicitly[Numeric[A]].plus(acc, h))
  }

  println(sum2(list))

  val st: Array[StackTraceElement] = Thread.currentThread().getStackTrace

}
