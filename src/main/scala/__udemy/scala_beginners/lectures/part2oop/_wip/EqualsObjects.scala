package __udemy.scala_beginners.lectures.part2oop._wip

object EqualsObjects extends App {

  class Wrapper(name: String, age: Int) {
    def canEqual(a: Any): Boolean = a.isInstanceOf[Wrapper]

    override def equals(that: Any): Boolean =
      that match {
        case that: Wrapper => that.canEqual(that) && this.hashCode == that.hashCode
        case _ => false
      }

    override def hashCode(): Int = {
      val state = Seq(name, age)
      //state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
      state.map(_.##)./:(0)((a, b) => 31 * a + b)
    }
  }

  val w1 = new Wrapper("zxc", 21)
  val w2 = new Wrapper("zxc", 21)

  println(w1 equals w2)
  println(w1 == w2) // links to equals
  println(w1 eq w2) // pointers
  println(w1.## == w2.##) // hash

}
