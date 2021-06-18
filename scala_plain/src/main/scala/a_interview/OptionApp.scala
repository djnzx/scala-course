package a_interview

object OptionApp extends App {

  sealed trait OptionX[A] {
    def flatMap[B](f: A => OptionX[B]): OptionX[B] = this match {
      case SomeX(a) => f(a)
      case _ => NoneX()
    }
    def map[B](f: A => B): OptionX[B] = flatMap(a => SomeX(f(a)))
    def filter(p: A => Boolean): OptionX[A] = flatMap(a => if (p(a)) SomeX(a) else NoneX())
    def flatten[AA](implicit ev: A <:< OptionX[AA]): OptionX[AA] = flatMap(identity[A])
    def getOrElse(other: => A): A = this match {
      case SomeX(a) => a
      case _ => other
    }
  }

  object OptionX {
    def some[A](a: A): OptionX[A] = SomeX(a)
    def none[A]: OptionX[A] = NoneX[A]()
  }

  case class SomeX[A](a: A) extends OptionX[A]
  case class NoneX[A]() extends OptionX[A]

  val x = OptionX.some(5)
  val w = x.map(_ + 1)
  val y = x.flatMap(a => OptionX.some(a + 10))
  val z = x.filter(_ > 10)

  println(y)
  println(z)

  val op66:   OptionX[Int] = OptionX.some(66)
  val opop66: OptionX[OptionX[Int]] = OptionX.some(op66)
  val op66f:  OptionX[Int] = opop66.flatten

//  OptionX.some(5).flatten
  OptionX.some(OptionX.some(5)).flatten
//  OptionX.none[Int].flatten
  OptionX.none[Nothing].flatten
  OptionX.none.flatten

  println(op66)
  println(opop66)
  println(op66f)

}
