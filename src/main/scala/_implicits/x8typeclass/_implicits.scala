import _implicits.x8typeclass.CanChat

package object _implicits {

  implicit val intCanChat: CanChat[Int] = new CanChat[Int] {
    override def chat_(a: Int): String = s"I'm an int (implicit val imported from other class) with a val:$a"
  }

}
