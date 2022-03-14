package typemember

import java.util.UUID

object TypeApp extends App {

  trait Creator[A] {
    def create(id: UUID): A
  }

  // using type without it real usage
  class Box[A](value: UUID) {
    def as(implicit creator: Creator[A]): A = creator.create(value)
  }

  val x1: Box[Int] = new Box[Int](UUID.randomUUID())
  val x2: Box[String] = new Box[String](UUID.randomUUID())

  implicit val c1: Creator[Int] = ???
  implicit val c2: Creator[String] = ???

  val v1: Int = x1.as
  val v2: String = x2.as

}
