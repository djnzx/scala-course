package cookbook.x011

object C11_05 extends App {
  val a = List(1,2,3)
  val b = List(4,5,6)
  val c1 = a ++ b
  val c2 = a ::: b
  val c3 = List.concat(a, b)


}
