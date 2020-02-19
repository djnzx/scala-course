package topics.monad_transform.monads

trait Monad[M[_]] {
  // lazy lift value to Monad
  def lift[A](a: => A): M[A]

  def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

  // map implementation based on flatMap  and lift
  def map[A, B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(a => lift[B](f(a)))
}
