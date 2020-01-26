package aa_cookbook.x010

class PP(val name: String) extends Ordered[PP] {
  override def toString = name
  override def compare(that: PP): Int =
    if (this.name == that.name) 0
    else if (this.name < that.name) -1
    else 1
}

object PPX extends App {
  val a = new PP("z")
  val z = new PP("a")
  val b = new PP("b")
  val ls = List(a,z,b)
  println(ls.sorted)
}
