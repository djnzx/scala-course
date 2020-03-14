package cats

object C087 extends App {
  import cats.Monad
  import cats.syntax.functor._
  import cats.syntax.flatMap._

  def sumSq[F[_]: Monad](ma: F[Int], mb: F[Int]): F[Int] = {
    ma.flatMap(a => mb.map(b => a + b))
  }

  def sumSq2[F[_]: Monad](ma: F[Int], mb: F[Int]): F[Int] = for {
    a <- ma
    b <- mb
  } yield a * b

  import cats.instances.list._
  val r1: List[Int] = sumSq(List(1,2,3), List(4,5,6))
  println(r1)

  import cats.instances.option._
  val r2 = sumSq[Option](Some(2), Some(3))
  println(r2)
}
