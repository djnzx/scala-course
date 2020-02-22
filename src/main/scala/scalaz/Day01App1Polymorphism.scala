package scalaz

object Day01App1Polymorphism extends App {

  // base idea
  trait Plus1[A] {
    def plus(a: A): A
  }

  def plus1[A <: Plus1[A]](a1: A, a2: A): A = a1.plus(a2)

  // far more better
  trait Plus[A] {
    def plus(a1: A, a2: A): A
  }

  def plus0[A](a1: A, a2: A)(implicit ev: Plus[A]): A = ev.plus(a1, a2)
  def plus[A: Plus](a1: A, a2: A): A = implicitly[Plus[A]].plus(a1, a2)

  implicit val plus_int: Plus[Int] = new Plus[Int] {
    override def plus(a1: Int, a2: Int): Int = a1 + a2
  }

  implicit val plus_str: Plus[String] = new Plus[String] {
    override def plus(a1: String, a2: String): String = s"$a1$a2"
  }

  val r1: Int = plus(1,1)
  val r2: String = plus("Hello ", "World")



}
