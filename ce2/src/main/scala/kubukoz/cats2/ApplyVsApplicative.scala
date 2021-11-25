package kubukoz.cats2

import cats.Applicative
import cats.Monoid
import cats.implicits._

/**   - Apply has Functor (map) + `ap`
  *   - Applicative has `pure`
  */
object ApplyVsApplicative extends App {

  def mapApply[K: Monoid]: Applicative[Map[K, *]] = new Applicative[Map[K, *]] {

//    override def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = fa.map { case (k, v) => (k, f(v)) }

    override def ap[A, B](ff: Map[K, A => B])(fa: Map[K, A]): Map[K, B] =
      ff.flatMap { case (k, f) =>
        fa.get(k) match {
          case Some(a) => Map(k -> f(a))
          case None    => Map.empty[K, B]
        }
      }

    // don't pass the laws
    def pure1[A](x: A): Map[K, A] = Map(Monoid.empty[K] -> x)
    def pure2[A](x: A): Map[K, A] = Map.empty

    override def pure[A](x: A): Map[K, A] = ???

  }

  val a: Map[Int, Boolean] = Map(2 -> true, 3 -> false)
  val b: Map[Int, String] = Map(3 -> "foo", 4 -> "bar")
  val c1a: Map[Int, String] = a *> b
  val c1b: Map[Int, Boolean] = a <* b
  val c2: Map[Int, (Boolean, String)] = (a, b).tupled
  val c3: Map[Int, String] = (a, b).mapN { (b, s) => b.toString + s }

  pprint.pprintln(a)
  pprint.pprintln(b)
  pprint.pprintln(c1a)
  pprint.pprintln(c1b)
  pprint.pprintln(c2)
  pprint.pprintln(c3)
}
