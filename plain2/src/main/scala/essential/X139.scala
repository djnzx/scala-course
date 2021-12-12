package essential

object X139 extends App {
  case class Pair[A, B](a: A, b: B)

  def x: Boolean => Any = (b: Boolean) => if (b) 123 else "abc"

  sealed trait XEither[A, B] {
    def fold[C](fa: A => C, fb: B => C): C = this match {
      case XLeft(l)  => fa(l)
      case XRight(r) => fb(r)
    }
    def map[C](fn: B => C): XEither[A, C] = this match {
      case XLeft(l)  => XLeft(l)
      case XRight(r) => XRight(fn(r))
    }
    def flatMap[C](fn: B => XEither[A, C]): XEither[A, C] = this match {
      case XLeft(l)  => XLeft(l)
      case XRight(r) => fn(r)
    }
  }
  case class XLeft[A, B](value: A) extends XEither[A, B]
  case class XRight[A, B](value: B) extends XEither[A, B]

  def is(input: Boolean): XEither[Int, String] =
    if (input) {
      XLeft[Int, String](123)
    } else {
      XRight[Int, String]("abc")
    }
  val xe1: XEither[Int, String] = is(true)
  val xe2: XEither[Int, String] = is(false)
  val xes1 = xe1.fold(v => (v + 1).toString, v => s">$v<")
  val xes2 = xe2.fold(v => (v + 1).toString, v => s">$v<")
  println(xes1)
  println(xes2)
}
