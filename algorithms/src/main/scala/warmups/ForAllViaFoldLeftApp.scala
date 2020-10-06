package warmups

object ForAllViaFoldLeftApp extends App {

  def forAll[A](xs: List[A])(p: A => Boolean): Boolean =
    xs.foldLeft(true)((acc: Boolean, a: A) => acc && p(a))

  val data = List(1,2,3)

  val b1: Boolean = forAll(data)(_ > 0) // true
  val b2: Boolean = forAll(data)(_ > 2) // false
  val b3: Boolean = forAll(data)(_ > 3) // false

  println(b1)
  println(b2)
  println(b3)
  assert(b1)
  assert(!b2)
  assert(!b3)

}
