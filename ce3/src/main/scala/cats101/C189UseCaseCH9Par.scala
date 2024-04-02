package cats101

import cats.Monoid

/**
  * map(F[A])(A=>B): F[B]
  * because of dependencies absence we can parallelize them
  *
  * to parallelize the reduction (B)((B,A)=>B) we need it to be associative:
  * reduce(a1, reduce(a2,a3)) == reduce(reduce(a1,a2), a3)
  * and to have identity element:
  * reduce(a1, identity) = reduce(identity, a1) = a1
  */
object C189UseCaseCH9Par extends App {

  def foldMap[A, B](data: Vector[A])(f: A => B)(implicit  mb: Monoid[B]): B = {
    println(Thread.currentThread().getName)
    val dataB: Vector[B] = data.map(f)
    val b: B = dataB.foldLeft(mb.empty)(mb.combine)
    b
  }

  import cats.instances.int._
  val r1: Int = foldMap(Vector(1,2,3,4,5))(_ * 10)
  println(r1)

  import cats.instances.string._
  val r2: String = foldMap(Vector(1,2,3,4,5))(_.toString+"! ")
  println(r2)
}
