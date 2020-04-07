package cats

import cats.instances.list._
import cats.syntax.applicative._ // pure
import cats.syntax.apply._       // mapN

object C176TraverseViaApplicative extends App {

  // traverse via traverse
  def listTraverseT[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    Traverse[List].traverse(list)(func)

  // traverse via applicative
  def zero[F[_]:Applicative, B]: F[List[B]] = List.empty[B].pure[F]

//  def listTraverseA[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
//    list.foldLeft(zero)((acc: F[List[B]], item: A) => {
//      val fb: F[B] = func(item)
//      val p: F[(List[B], B)] = Apply[F].product(acc, fb)
//      val r: F[List[B]] = Apply[F].map(p)(t => t._1 :+ t._2)
//      r
//    })

//  def listTraverseA2[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
//    list.foldLeft(zero)((acc: F[List[B]], item: A) => {
//      val fb: F[B] = func(item)
//      val r: F[List[B]] = (acc, fb).mapN(_ :+ _)
//      r
//    })
//
//  def listTraverseA4[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
//    list.foldLeft(zero)((acc: F[List[B]], item: A) => {
//      val fb: F[B] = func(item)
//      val r: F[List[B]] = Semigroupal.map2(acc, fb)(_ :+ _)
//      r
//    })
//
//  def listTraverseA3[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
//    list.foldLeft(zero)((acc: F[List[B]], el: A) =>
//      Apply[F].map(Apply[F].product(acc, func(el)))(t => t._1 :+ t._2)
//    )
//
//  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] = ???

//  (List(Vector(1,2), Vector(3,4)))
}
