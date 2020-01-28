package aa_fp

object Fps003MethodVsFunc extends App {

  val double1 = (i: Int) => i * 2
  def double2(i: Int): Int = i * 2
  val double3 = double2 _

  val l = List(1,2,3)

  val set1 = Set(double1, double2 _)
  val set2: Set[Int => Int] = Set(double1, double2)

  val l11 = l.map(double1)
  val l12 = l.map(double2)
  val l13 = l.map(double3)

  println(l11)
  println(l12)
  println(l13)
}
