package multiple

object MultipleParent extends App {

  sealed trait A
  sealed trait B
  sealed trait C

  case class Q() extends A
  case class W() extends A with B
  case class E() extends B
  case class R() extends B with C
  case class T() extends C
  case class U() extends C with A

  val a: A = ???
  a match {
    case Q() => ???
    case W() => ???
    case U() => ???
  }

  val b: B = ???
  b match {
    case W() => ???
    case E() => ???
    case R() => ???
  }

  val c: C = ???
  c match {
    case R() => ???
    case T() => ???
    case U() => ???
  }
}
