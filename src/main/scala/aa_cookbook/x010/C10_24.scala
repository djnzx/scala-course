package aa_cookbook.x010

object C10_24 extends App {
  val r = 1 to 100
  println(r)
  println(r.view)
  println(r.view.force)

}
