package fp_red.red04
import scala.{None => _, Option => _, Some => _} 

sealed trait Option[+A] {
  
  def map[B](f: A => B): Option[B] = this match {
    case Some(a) => Some(f(a))
    case None    => None
  }
  
  def flatMap[B](f: A => Option[B]): Option[B] = this match {
    case Some(a) => f(a)
    case None    => None
  }
  
  // actually kind of flatten, fold, extract the data
  def getOrElse[A2 >: A](default: => A2): A2 = this match {
    case Some(a) => a
    case None    => default
  }

  def map_via_flatMap[B](f: A => B): Option[B] =
    flatMap { a => Some(f(a)) }

  // intermediate step Option[Option[A]]
  def flatMap_via_map[B](f: A => Option[B]): Option[B] =
    map(f) getOrElse None

  // keep monad unopened, just set it value 
  def orElse[A2 >: A](ob: => Option[A2]): Option[A2] = this match {
    case Some(_) => this
    case None    => ob
  }

  // filter via pattern matching
  def filter(p: A => Boolean): Option[A] = this match {
    case Some(a) if p(a) => this
    case _               => None
  }

  // filter via FlatMap
  def filter_via_flatMap(p: A => Boolean): Option[A] =
    flatMap { a => if (p(a)) Some(a) else None }

  // intermediate step Option[Option[A]]
  def orElse_via_map[A2 >: A](ob: => Option[A2]): Option[A2] =
    map(a => Some(a)) getOrElse ob

}
case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]

object JustSyntax {
  def failingFn(i: Int): Int = {
    // `val y: Int = ...` declares `y` as having type `Int`, and sets it equal to the right hand side of the `=`.
    val y: Int = throw new Exception("fail!")
    try {
      val x = 42 + 5
      x + y
    }
    catch { case e: Exception => 43 }
  }

  def failingFn2(i: Int): Int = {
    try {
      val x = 42 + 5
      x + ((throw new Exception("fail!")): Int) // A thrown Exception can be given any type; here we're annotating it with the type `Int`
    }
    catch { case e: Exception => 43 }
  }
}

object ExerciseBasic {
  def mean(xs: Seq[Double]): Option[Double] =
    if (xs.isEmpty) None
    else Some(xs.sum / xs.length)

  def dist(x: Double, mean: Double) = Math.pow(x - mean, 2)

  def variance1(xs: Seq[Double]): Option[Double] =
    mean(xs) flatMap { m => mean(xs.map(dist(_, m))) }

  def variance2(xs: Seq[Double]): Option[Double] = for {
    avg <- mean(xs)
    ds = xs.map(x => dist(x, avg))
    res <- mean(ds)
  } yield res
  
}
