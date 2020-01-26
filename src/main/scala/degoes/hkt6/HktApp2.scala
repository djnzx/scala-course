package degoes.hkt6

object HktApp2 extends App {

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  val fl = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  val fs = new Functor[Set] {
    override def map[A, B](fa: Set[A])(f: A => B): Set[B] = fa.map(f)
  }

  val l1: List[Int] = List(1,2,3)
  val l2: List[Int] = fl.map(l1)(x => x + 1)
  println(l1)
  println(l2)
  println(fs.map(Set(1,2,3))(x => x % 2))

  case class Composite[F[_], G[_], A](run: F[G[A]])
  case class Product[F[_], G[_], A](run: (F[A], G[A]))
  // sum
  case class Coproduct[F[_], G[_], A](run: Either[F[A], G[A]])

  trait Apply[F[_]] extends Functor[F] {
    def ap[A, B](fa: F[A])(fab: F[A => B]): F[B]
  }
  // lift any value to Applicative
  trait Applicative[F[_]] extends Apply[F] {
    def point[A](a: A): F[A]
  }

  // actually flatMap
  trait Bind[F[_]] extends Apply[F] {
    def bind[A, B](fa: F[A])(afb: A => F[B]): F[B]
  }

  trait Monad[F[_]] extends Applicative[F] with Bind[F]


}
