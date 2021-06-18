package a_interview

object OptionApp extends App {

  sealed trait OptionX[A] {
    def flatMap[B](f: A => OptionX[B]): OptionX[B]
    def map[B](f: A => B): OptionX[B] = flatMap(a => SomeX(f(a)))
    def filter(p: A => Boolean): OptionX[A] = flatMap(a => if (p(a)) SomeX(a) else NoneX())
    def flatten(implicit ev: A <:< OptionX[A]): OptionX[A] = flatMap(a => a)
    def getOrElse(other: => A): A = this match {
      case SomeX(a) => a
      case NoneX() => other
    }
  }

  object OptionX {
    def some[A](a: A): OptionX[A] = SomeX(a)
    def none[A]: OptionX[A] = NoneX[A]()
  }

  case class SomeX[A](a: A) extends OptionX[A] {
    override def flatMap[B](f: A => OptionX[B]): OptionX[B] = f(a) match {
      case SomeX(a) => SomeX(a)
      case NoneX() => NoneX()
    }
  }

  case class NoneX[A]() extends OptionX[A] {
    override def flatMap[B](f: A => OptionX[B]): OptionX[B] = NoneX[B]()
  }

  val x = OptionX.some(5)
  val w = x.map(_ + 1)
  val y = x.flatMap(a => OptionX.some(a + 10))
  val z = x.filter(_ > 10)

  println(y)
  println(z)

}
