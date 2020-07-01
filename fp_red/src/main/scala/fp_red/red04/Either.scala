package fp_red.red04

import scala.{Either => _, Left => _, Right => _}

sealed trait Either[+E, +A] {
  // map basic
  def map[B](f: A => B): Either[E, B] = this match {
    case Right(a) => Right(f(a))
    case Left(e)  => Left(e)
  }
  // flatMap basic
  def flatMap[E2 >: E, B](f: A => Either[E2, B]): Either[E2, B] = this match {
    case Right(a) => f(a)
    case Left(e)  => Left(e)
  }
  // extractor, b is lazy
  def orElse[E2 >: E, B >: A](b: => Either[E2, B]): Either[E2, B] = this match {
    case Right(a) => Right(a)
    case Left(_)  => b
  }
  // plain version
  def map2[E2 >: E, B, C](b: => Either[E2, B])(f: (A, B) => C): Either[E2, C] =
    this match {
      case Right(a) => b match {
        case Right(b) => Right(f(a,b))
        case Left(ee) => Left(ee)
      }
      case Left(e)  => Left(e)
  }
  
  def map2_v2[E2 >: E, B, C](b: Either[E2, B])(f: (A, B) => C): Either[E2, C] =
    (this, b) match {
      case (Right(a), Right(b)) => Right(f(a,b))
      case (Left(e), _)         => Left(e)
      case (_, Left(e2))        => Left(e2)
    }
  
  def map2_via_flatMap[E2 >: E, B, C](b: Either[E2, B])(f: (A, B) => C): Either[E2, C] =
    flatMap(a => b.map(b => f(a,b)))
  
  def map2_via_flatMap_for[E2 >: E, B, C](b: Either[E2, B])(f: (A, B) => C): Either[E2, C] = for {
    ar <- this
    br <- b
  } yield f(ar, br)
  
}

case class Left[+E](get: E) extends Either[E, Nothing]
case class Right[+A](get: A) extends Either[Nothing, A]

object EitherBasicExercise {

  def dist(x: Double, mean: Double) = Math.pow(x - mean, 2)

  def mean(xs: Seq[Double]): Either[String, Double] =
    if (xs.isEmpty) Left("mean of empty list!")
    else Right(xs.sum / xs.length)

  def safeDiv(x: Int, y: Int): Either[Exception, Int] =
    try Right(x / y)
    catch { case e: Exception => Left(e) }

  def Try[A](a: => A): Either[Exception, A] =
    try Right(a)
    catch { case e: Exception => Left(e) }

  def variance1(xs: Seq[Double]): Either[String, Double] =
    mean(xs) flatMap { m => mean(xs.map(dist(_, m))) }

  def variance2(xs: Seq[Double]): Either[String, Double] = for {
    avg <- mean(xs)
    ds = xs.map(x => dist(x, avg))
    res <- mean(ds)
  } yield res

}
