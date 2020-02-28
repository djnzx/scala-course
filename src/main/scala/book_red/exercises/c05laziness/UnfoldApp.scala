package book_red.exercises.c05laziness

import scala.annotation.tailrec
import scala.util.Random

object UnfoldApp extends App {

  def unfold[S,A](zero: S)(f: S => Option[(S,A)]): List[A] =
    f(zero) match {
      case None => Nil
      case Some((s,a)) => a :: unfold(s)(f)
    }

  def unfoldTR[S,A](zero: S)(f: S => Option[(S,A)]): List[A] = {
    @tailrec
    def go(zero: S)(acc: List[A])(f: S => Option[(S,A)]): List[A] =
      f(zero) match {
        case None => acc
        case Some((s,a)) => go(s)(a::acc)(f)
      }
    go(zero)(Nil)(f) reverse
  }

  val randoms: Seq[(Int, Int)] = unfoldTR(0){ s: Int =>
    val ns = s+1
    if (ns < 10) Some((ns, (ns, Random.nextInt(100)))) else None
  }
  println(randoms)

}
