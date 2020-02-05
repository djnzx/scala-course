package aa_fp

object Fps006ImplicitScope extends App {

  def inc(origin: Int)(implicit delta: Int): Int = origin + delta

  object Scope1 {
    implicit val delta1: Int = 1
  }

  object Scope2 {
    implicit val delta2: Int = 2
  }

  import Scope1._

  println(inc(5)) // 6

  def nested() = {
    println(inc(5)) // 6
  }

  nested()

  val l0: Seq[Int] = List(1,2,3)
  val l1: Seq[Int] = l0.flatMap(x => List(x, x+1))
  println(l1)

  val l2: Seq[List[Int]] = l0.map(x => List(x, x+1))
  val l3: Seq[Int] = l2.flatten
  println(l3)

  val f0 = Some(1)
  val f1: Option[List[Int]] = f0.map(x => List(x-1, x+1))
}
