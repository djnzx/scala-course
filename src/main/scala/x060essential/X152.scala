package x060essential

object X152 extends App {

  /** Variance:
    * invariance: A === B
    * covariance: +A === B extends A
    * contravariant: -A === A extends B
    *
    * Func􏰀ons are
    * - contravariant in terms of their arguments and
    * - covariant in terms of their return type
    */

  sealed trait Maybe[A]
  final case class Full[A](value: A) extends Maybe[A]
  final case class Empty[A]() extends Maybe[A]
  val perhaps1: Maybe[Int] = Full(123)
  val perhaps2: Maybe[Int] = Empty[Int]()

  sealed trait XMaybe[+A]
  final case class XFull[A](value: A) extends XMaybe[A]
  case object XEmpty extends XMaybe[Nothing]

  val perhaps3: XMaybe[Int] = XFull(123)
  val perhaps4: XMaybe[Int] = XEmpty


}
