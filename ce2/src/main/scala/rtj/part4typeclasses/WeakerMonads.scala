package rtj.part4typeclasses

import cats.Applicative
import cats.Apply

object WeakerMonads {

  trait MyFlatMap[M[_]] extends Apply[M] {
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]

    // TODO: implement ap in terms of flatMap & map
    def ap[A, B](wf: M[A => B])(wa: M[A]): M[B] =
      flatMap(wa) { a: A => map(wf) { f: (A => B) => f(a) } }
    //         |     |          /        \            \/
    //         |     |      M[A=>B]      A=>B         B
    //         |     |      \________________________/
    //       M[A]    A   =>           M[B]
  }

  trait MyMonad[M[_]] extends Applicative[M] with MyFlatMap[M] {
    override def map[A, B](ma: M[A])(f: A => B): M[B] =
      flatMap(ma)(x => pure(f(x)))
  }

  import cats.FlatMap
  import cats.syntax.flatMap._ // flatMap extension method
  import cats.syntax.functor._ // map extension method

  def getPairs[M[_]: FlatMap, A, B](numbers: M[A], chars: M[B]): M[(A, B)] = for {
    n <- numbers
    c <- chars
  } yield (n, c)

  def main(args: Array[String]): Unit = {}
}
