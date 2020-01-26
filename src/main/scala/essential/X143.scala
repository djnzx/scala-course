package essential

object X143 extends App {

  sealed trait XOption[A] {
    def fold[B](f: A => B, vnone: B): B = this match {
      case XSome(v) => f(v)
      case XNone() => vnone
    }
  }
  final case class XSome[A](v: A) extends XOption[A]
  final case class XNone[A]() extends XOption[A]

  val xo1: XOption[Int] = XSome(123)
  val xo2: XOption[Int] = XNone[Int]()
  val xos1: String = xo1.fold(_.toString, "<n>")
  val xos2: String = xo2.fold(_.toString, "<n>")
  println(xos1)
  println(xos2)

}
