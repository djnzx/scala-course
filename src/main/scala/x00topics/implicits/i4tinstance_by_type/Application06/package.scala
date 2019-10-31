package x00topics.implicits.i4tinstance_by_type

package object Application06 {
  implicit val intCanChat: CanChat[Int] = new CanChat[Int] {
    override def chat_(a: Int): String = s"I'm an int (implicit val imported from other class) with a val:$a"
  }
}
