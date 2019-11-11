package x060essential

object X156 extends App {
  // covariance declaration
  case class Box[+A](value: A) {
    // covariance opening
    // we must introduce new type to solve covariance problem
    // class AA - any class which extends A
    // that's only way to preserve the result type
    // in Java we can write '? extends A' and immediately erase the result type, That's bad
    def set[AA >: A](a: AA): Box[AA] = Box(a)
  }

  trait XEither[+A, +B] {
    def fold[C](fa: A => C, fb: B => C): C = this match {
      case XLeft(l) => fa(l)
      case XRight(r) => fb(r)
    }
//    def map[C](fn: B => C): XEither[A, C] = this match {
//      case XLeft(l) => XLeft(l)
//      case XRight(r) => XRight(fn(r))
//    }
    def flatMap[AA >: A, C](fn: B => XEither[AA, C]): XEither[AA, C] = this match {
      case XLeft(l) => XLeft(l)
      case XRight(r) => fn(r)
    }
  }
  case class XLeft[A](value: A) extends XEither[A, Nothing]
  case class XRight[B](value: B) extends XEither[Nothing, B]

  // same declaration
  val x1: XLeft[Int] = XLeft(123)
  val x2: XEither[Int, Nothing] = XLeft(123)
  // same declaration
  val x3: XRight[String] = XRight("Hello")
  val x4: XEither[Nothing, String] = XRight("Hello")
}
