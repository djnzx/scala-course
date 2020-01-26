package aa_cookbook.x010

object C10_09 extends App {
  val mm = Map(1 -> "Jan", 2 -> "Feb", 3 -> "Mar")
  mm.foreach({
    case (k, v) => println(s"$k, $v")
  })

  val pp = (k: Int, v: String) => println(s"$k, $v")

}
