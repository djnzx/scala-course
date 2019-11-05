package x060essential

object X123 extends App {
  final case class Box[A](value: A)

  val v1: Box[Int] = Box(1)
  val v2: Box[String] = Box("hello")

}
