package cats101.c077monads

import cats.Id

object C091IdApp extends App {

  /**
    * map and FlatMap on Identity monad are absolutely the same!
    *
    */
  def pure[A](value: A): Id[A] = value

  def map[A, B](initial: Id[A])(func: A => B): Id[B] = func(initial)

  def flatMap[A, B](initial: Id[A])(func: A => Id[B]): Id[B] = func(initial)

}
