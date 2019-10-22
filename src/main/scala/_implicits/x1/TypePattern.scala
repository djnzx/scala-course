package _implicits.x1

object TypePattern extends App {

  trait Squash[T] {
    def squash(a: T, b: T): T
  }

  object Squash {
    def apply[T](implicit ev: Squash[T]): Squash[T] = ev
  }

  implicit class Squashify[T](value: T)(implicit ev: Squash[T]) {
    def squash(another: T): T = ev.squash(value, another)
  }

  implicit val squashInt: Squash[Int] = new Squash[Int] {
    override def squash(a: Int, b: Int): Int = a + b
  }

  implicit def squashOpt[U](implicit ev: Squash[U]) = new Squash[Option[U]] {
    override def squash(a: Option[U], b: Option[U]) = for {
      v1 <- a
      v2 <- b
    } yield ev.squash(v1, v2)
  }

  implicitly[Squash[Option[Int]]].squash(Some(1), Some(3))

  val x1: Option[Int] = Squash[Option[Int]].squash(Some(1), Some(3))
  val x2: Option[Int] = Option(1) squash Option(2)


}
