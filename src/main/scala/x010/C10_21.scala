package x010

object C10_21 extends App {
  val l1 = List.range(1,10)
  val l2 = List.range(5,15)

  println("union:", l1 union l2)
  println("diff l1 l2:", l1 diff l2)
  println("diff l2 l1:", l2 diff l1)


  val l3 = l1 ++ l2
  val l31 = l1 ::: l2 // prepend
  println(l3)
  println(l31)
  val l4 = l3.distinct
  println(l4)

  class Person(first: String, last: String) {
    override def toString: String = s"$first $last"

    def canEqual(a: Any) = a.isInstanceOf[Person]

    override def equals(obj: Any): Boolean = obj match {
      case obj: Person => obj.canEqual(this) && this.## == obj.##
      case _ => false
    }

    override def hashCode(): Int = Seq(first, last).foldLeft(1)((a, b) => a.## * 31 + b.##)
  }
  val p1 = new Person("Alex", "R")
  val p2 = new Person("Alex", "R")
  val l = List(p1, p2)
  println(l)
  val ld = l.distinct
  println(ld)
}
