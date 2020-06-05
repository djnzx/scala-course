package hackerrankfp.d200604_09

object PosApp extends App {
  case class A(a: Int)
  val v = Vector(A(2), A(1), A(2), A(1))
  val r = v.search(A(1))((x,y) => x.a - y.a)
  println(r)
}
